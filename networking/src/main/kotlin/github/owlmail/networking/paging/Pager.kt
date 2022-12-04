package github.owlmail.networking.paging

import java.lang.Integer.min
import kotlin.math.abs
import kotlin.math.max
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.runBlocking

/**
 * Using Custom Paging due to 3 main reasons
 * 1. Issues with Selective Update
 * 2. This clears all the buildModel cache from Epoxy
 * 3. In Epoxy can't map one item to List of items
 */

class Pager<Key : Any, ItemType : Any, PayLoadType : Any, ErrorType : Any>(
    viewModelScope: CoroutineScope,
    pagingConfig: PagingConfig<Key>,
    private val pagingDataSource: PagingDataSource<Key, ItemType, PayLoadType, ErrorType>
) {

    /**
     * Keeps this as a equivalent to Int keyed paging
     */
    private var currentPageIndex = 0
    private var previousPageIndex = -1
    private var nextPageIndex = 0

    /**
     * Keep the index where the list is separated for bidirectional case
     */
    private var startIndex = 0

    @PagingDirection
    private val initialPagingDirection = pagingConfig.initialPagingDirection
    private val initialOffset = pagingConfig.initialOffSet
    private val loadSize = pagingConfig.loadSize
    private val initialLoadMultiplier = pagingConfig.initialLoadMultiplier

    /**
     * Storing the latest value for both of these
     */
    private var prevOffset: Key? = initialOffset
    private var nextOffset: Key? = initialOffset

    /**
     * A scope that is alive till the lifecycle is alive
     */
    private val coroutineScope = viewModelScope + Dispatchers.IO

    private val nullPaddedList = mutableListOf<ItemType?>()

    private val workMap = mutableMapOf<Int, Job>()

    private val pagedList = MutableStateFlow<List<ItemType>>(emptyList())
    private val pageStatus =
        MutableStateFlow<PageStatus<Key, ItemType, PayLoadType, ErrorType>>(PageStatus.Default)

    fun getPageStatus() = pageStatus.asStateFlow()

    fun getPaginatedList() = pagedList.asStateFlow()

    /**
     * Loads multiple pages at once
     */
    fun loadInitial() = refresh(true)

    /**
     * Loads the page acc. to direction
     */
    fun load(@PagingDirection loadDirection: Int) = startWork(PagerWorkType.Load) {
        loadInternal(loadDirection)
    }

    fun refresh(clearCache: Boolean = false) = startWork(PagerWorkType.Refresh) {
        refreshInternal(clearCache)
    }

    fun retry() = startWork(PagerWorkType.Retry) {
        retryInternal()
    }

    fun selectivelyRefreshItems(itemsList: List<Pair<ItemType, PayLoadType>>) =
        startWork(PagerWorkType.SelectiveRefresh) {
            selectiveRefreshInternal(itemsList)
        }

    /**
     * Starts the work and keeps the job, to cancel previous one in case of repeated request
     */
    private fun startWork(@PagerWorkType workType: Int, work: suspend () -> Unit) {
        workMap[workType]?.cancel()
        val job = Job()
        workMap[workType] = job
        (coroutineScope + job).launch {
            work.invoke()
        }.invokeOnCompletion {
            it?.let {
                pageStatus.value = PageStatus.Default
            }
        }
    }

    /**
     * Validate type of retry & take action
     */
    private suspend fun retryInternal() {
        when (val state = pageStatus.value) {
            is PageStatus.Error -> {
                loadInternal(state.loadDirection)
            }
            is PageStatus.SelectiveRefresh.Error -> {
                loadSelectiveItems(state.items)
            }
            else -> return
        }
    }

    /**
     * Reset states & trigger load acc. to [clearCache] flag
     */
    private suspend fun refreshInternal(clearCache: Boolean) {
        if (clearCache) {
            refreshWithOutExistingCache()
        } else {
            refreshWithExistingCache()
        }
    }

    /**
     * Keeps the cache & replaces each page in the range [previousPageIndex] to [nextPageIndex]
     * which is equivalent of [prevOffset] to [nextOffset]
     * We start from [initialOffset] to avoid unstable key case
     * Note :- this can take time if the range is large
     */
    private suspend fun refreshWithExistingCache() {
        val oldNextPageIndex: Int
        val oldPreviousPageIndex: Int
        when (initialPagingDirection) {
            PagingDirection.Upward -> {
                oldNextPageIndex = nextPageIndex
                oldPreviousPageIndex = min(previousPageIndex + 1, 0 - initialLoadMultiplier)
            }
            PagingDirection.Downward -> {
                oldNextPageIndex = max(nextPageIndex, initialLoadMultiplier)
                oldPreviousPageIndex = previousPageIndex + 1
            }
            else -> return
        }
        resetAllState()
        for (i in oldPreviousPageIndex until 0) {
            loadInternal(PagingDirection.Upward)
        }
        for (i in 0 until oldNextPageIndex) {
            loadInternal(PagingDirection.Downward)
        }
    }

    /**
     * Deletes the existing list & reload initial pages
     */
    private suspend fun refreshWithOutExistingCache() {
        nullPaddedList.removeAll { true }
        resetAllState()
        loadInitialInternal()
    }

    /**
     * Reset all state, except paged list to avoid empty screen
     * This also excludes stopping the previous work, to fix it
     * we keep the job in a map
     */
    @Synchronized
    private fun resetAllState() {
        prevOffset = initialOffset
        nextOffset = initialOffset
        previousPageIndex = -1
        nextPageIndex = 0
        currentPageIndex = 0
        startIndex = 0
        pageStatus.value = PageStatus.Default
    }

    /**
     * Trigger selective refresh only when it is in a non-loading state, else queue it
     */
    private suspend fun selectiveRefreshInternal(itemsList: List<Pair<ItemType, PayLoadType>>) {
        when (pageStatus.value) {
            is PageStatus.Loading, is PageStatus.SelectiveRefresh.Loading -> {
                queueLoadAfterPageState(itemsList)
            }
            else -> {
                loadSelectiveItems(itemsList)
            }
        }
    }

    /**
     * Queues the job, when the work is not in loading state; execute it
     */
    private fun queueLoadAfterPageState(items: List<Pair<ItemType, PayLoadType>>) {
        val job = Job()
        pageStatus.onEach {
            when (it) {
                !is PageStatus.Loading, !is PageStatus.SelectiveRefresh.Loading -> {
                    loadSelectiveItems(items)
                    job.cancel()
                }
            }
        }.launchIn(coroutineScope + job)
    }

    /**
     * Does a selective refresh to items, stores previous state,
     * so on completion it can restore it back
     */
    private suspend fun loadSelectiveItems(
        itemsList: List<Pair<ItemType, PayLoadType>>
    ) {
        updateSelectiveRefreshPageStatusToLoading(itemsList)
        val result = pagingDataSource.selectivelyRefreshItems(itemsList)
        updateSelectiveRefreshPageStatusToSuccessOrError(result, itemsList)
    }

    /**
     * updates the pageStatus to Loading
     */
    private fun updateSelectiveRefreshPageStatusToLoading(
        itemsList: List<Pair<ItemType, PayLoadType>>
    ) {
        val items = itemsList.map { it.first }
        pageStatus.value = PageStatus.SelectiveRefresh.Loading(items)
    }

    /**
     * updates the pageStatus to success or error
     */
    private fun updateSelectiveRefreshPageStatusToSuccessOrError(
        result: SelectivePageResult<ItemType, PayLoadType, ErrorType>,
        itemsList: List<Pair<ItemType, PayLoadType>>
    ) {
        when (result) {
            is SelectivePageResult.Success -> {
                val page = PageStatus.SelectiveRefresh.Success(itemsList, result.items)
                pageStatus.value = page
                selectivelyUpdatePagedList(page)
            }
            is SelectivePageResult.Error -> {
                pageStatus.value = PageStatus.SelectiveRefresh.Error(itemsList, result.error)
            }
        }
    }

    /**
     * List updation logic for selective update
     */
    @Synchronized
    private fun selectivelyUpdatePagedList(
        page: PageStatus.SelectiveRefresh.Success<ItemType, PayLoadType>
    ) {
        val oldItems = page.request.map { it.first }
        val itemMap = pagingDataSource.mapOldItemsToNewItems(oldItems, page.data)
        itemMap.forEach {
            nullPaddedList.replace(it.key, it.value)
        }
        pagedList.value = nullPaddedList.filterNotNull()
    }

    @Synchronized
    private fun MutableList<ItemType?>.replace(old: ItemType, new: ItemType?) {
        val index = indexOf(old)
        if (index > 0) {
            set(index, new)
        }
    }

    /**
     * Loads multiple pages acc. to [initialLoadMultiplier]
     */
    private suspend fun loadInitialInternal() {
        for (i in 0 until initialLoadMultiplier) {
            loadInternal(initialPagingDirection)
        }
    }

    /**
     * Triggers new page load, when it is in Loaded, NoMorePages, Default or Error condition,
     * in NoMorePage condition we check for bidirectional paging
     */
    private suspend fun loadInternal(@PagingDirection loadDirection: Int) {
        when (pageStatus.value) {
            is PageStatus.SelectiveRefresh.Loading -> {
                queueLoadAfterPageState(loadDirection)
            }
            !is PageStatus.Loading -> {
                loadPage(loadDirection)
            }
            else -> return
        }
    }

    /**
     * Queues the job, when the work is not in loading state; execute it
     */
    private suspend fun queueLoadAfterPageState(@PagingDirection loadDirection: Int) {
        val job = Job()
        pageStatus.onEach {
            if (it !is PageStatus.SelectiveRefresh.Loading) {
                coroutineScope.launch { loadPage(loadDirection) }
                job.cancel()
            }
        }.launchIn(coroutineScope + job)
    }

    /**
     * Loads the page and updates pageStatus
     */
    private suspend fun loadPage(@PagingDirection loadDirection: Int) {
        val currentOffset = getCurrentOffset(loadDirection) ?: return
        updatePageStatusToLoading(currentOffset, loadDirection)
        val result = when (loadDirection) {
            PagingDirection.Upward -> pagingDataSource.loadUpward(currentOffset, loadSize)
            PagingDirection.Downward -> pagingDataSource.loadDownward(currentOffset, loadSize)
            else -> return
        }
        updatePageStatusToSuccessOrError(currentOffset, result, loadDirection)
    }

    /**
     * Updates the status to loading
     */
    private fun updatePageStatusToLoading(currentOffset: Key, loadDirection: Int) {
        pageStatus.value = when {
            currentOffset == initialOffset && loadDirection == initialPagingDirection -> PageStatus.Loading.FirstPageLoading
            loadDirection == PagingDirection.Downward -> PageStatus.Loading.NextPageLoading
            loadDirection == PagingDirection.Upward -> PageStatus.Loading.PreviousPageLoading
            else -> PageStatus.Default
        }
    }

    /**
     * Updates the status in case of load complete
     */
    private fun updatePageStatusToSuccessOrError(
        currentOffset: Key,
        result: PageResult<Key, ItemType, ErrorType>,
        loadDirection: Int
    ) {
        when (result) {
            is PageResult.Success -> {
                updateOffsetAndPageIndex(currentOffset, loadDirection, result)
                updatePageStatusToSuccess(currentOffset, loadDirection, result)
            }
            is PageResult.Error -> {
                updatePageStatusToError(currentOffset, loadDirection, result)
            }
        }
    }

    /**
     * Updates the status after success
     */
    private fun updatePageStatusToSuccess(
        currentOffset: Key,
        @PagingDirection loadDirection: Int,
        result: PageResult.Success<Key, ItemType>
    ) {
        val page = when {
            currentOffset == initialOffset && result.items.isNullOrEmpty() -> {
                PageStatus.Success.EmptyData(currentOffset, loadDirection)
            }
            getCurrentOffset(loadDirection) == null -> {
                PageStatus.Success.NoMorePages(currentOffset, loadDirection, result.items)
            }
            else -> PageStatus.Success.PageLoaded(currentOffset, loadDirection, result.items)
        }
        pageStatus.value = page
        updatePagedList(page)
    }

    /**
     * Updates the status on Error
     */
    private fun updatePageStatusToError(
        currentOffset: Key,
        loadDirection: Int,
        result: PageResult.Error<ErrorType>
    ) {
        pageStatus.value = PageStatus.Error(currentOffset, loadDirection, result.error)
    }

    /**
     * updates the offset & some internal keys
     */
    @Synchronized
    private fun updateOffsetAndPageIndex(
        currentOffset: Key,
        @PagingDirection loadDirection: Int,
        result: PageResult.Success<Key, ItemType>
    ) {
        when (loadDirection) {
            PagingDirection.Upward -> {
                currentPageIndex = previousPageIndex
                previousPageIndex--
                startIndex += loadSize
                prevOffset = result.prevKey
                if (currentOffset == initialOffset) {
                    nextOffset = result.nextKey
                }
            }
            PagingDirection.Downward -> {
                currentPageIndex = nextPageIndex
                nextPageIndex++
                nextOffset = result.nextKey
                if (currentOffset == initialOffset) {
                    prevOffset = result.prevKey
                }
            }
            else -> return
        }
    }

    /**
     * Updates the list in case of full page
     */
    @Synchronized
    private fun updatePagedList(page: PageStatus.Success<Key, ItemType>) = runBlocking {
        val currentPageOffset = currentPageIndex * loadSize
        when {
            /**
             * Replace old page in case of Downward
             */
            currentPageOffset >= startIndex && nextPageIndex * loadSize <= nullPaddedList.size -> {
                val currentIndex = currentPageOffset + startIndex
                page.mapToNullPaddedList().forEachIndexed { index, itemType ->
                    nullPaddedList[currentIndex + index] = itemType
                }
                if (page is PageStatus.Success.NoMorePages) {
                    val newEnd = currentIndex + loadSize
                    for (i in newEnd..nullPaddedList.lastIndex) {
                        nullPaddedList.removeAt(i)
                    }
                }
            }
            /**
             * Add new page at the end for Downward case
             */
            currentPageOffset >= startIndex -> {
                nullPaddedList.addAll(page.mapToNullPaddedList())
            }
            /**
             * Replace old page in case of Upward
             */
            abs(previousPageIndex + 1) * loadSize <= startIndex -> {
                val currentIndex = startIndex - abs(currentPageOffset)
                page.mapToNullPaddedList().reversed().forEachIndexed { index, itemType ->
                    nullPaddedList[currentIndex + index] = itemType
                }
                if (page is PageStatus.Success.NoMorePages) {
                    for (i in 0 until currentIndex) {
                        nullPaddedList.removeAt(i)
                    }
                }
            }
            /**
             * Add new page at the start for Upward case
             */
            else -> {
                nullPaddedList.addAll(0, page.mapToNullPaddedList())
            }
        }
        pagedList.value = nullPaddedList.filterNotNull()
    }

    /**
     * We store a null padded list to have proper pagination
     */
    @Synchronized
    private fun PageStatus.Success<Key, ItemType>.mapToNullPaddedList(): List<ItemType?> {
        val temporaryList = mutableListOf<ItemType?>()
        for (i in 0 until loadSize) {
            temporaryList.add(data?.getOrNull(i))
        }
        return temporaryList
    }

    /**
     * returns the offset
     */
    private fun getCurrentOffset(@PagingDirection loadDirection: Int) = when (loadDirection) {
        PagingDirection.Downward -> nextOffset
        PagingDirection.Upward -> prevOffset
        else -> null
    }
}
