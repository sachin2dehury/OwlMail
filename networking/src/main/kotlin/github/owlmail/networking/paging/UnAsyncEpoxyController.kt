package github.owlmail.networking.paging

/**
 * Using Custom Paging due to 3 main reasons
 * 1. Issues with Selective Update
 * 2. This clears all the buildModel cache from Epoxy
 * 3. In Epoxy can't map one item to List of items
 *
 * override the method [buildItemModels] to add your items,
 * [addFooterModels] to add footer & [addHeaderModels] to add header
 *
 * snapshot provides the data in epoxy
 *
 * Avoiding Differ since Epoxy uses [AsyncEpoxyDiffer] already,
 * so seems like a overhead.
 */

// abstract class UnAsyncEpoxyController<T : Any> : AsyncEpoxyController() {
//
//    private var pagedData: List<T>? = null
//    private var selectivelyRefreshingItems: List<T>? = null
//
//    override fun buildModels() {
//        val isDataEmpty = pagedData.isNullOrEmpty()
//        addHeaderModels(isDataEmpty)
//        pagedData?.forEachIndexed { index, item ->
//            val isItemRefreshing = selectivelyRefreshingItems?.contains(item) ?: false
//            buildItemModels(item, index, isItemRefreshing)
//        }
//        addFooterModels(isDataEmpty)
//    }
//
//    @Synchronized
//    fun submitPagedList(pagedList: List<T>? = null) {
//        pagedData = pagedList
//        requestModelBuild()
//    }
//
//    @Synchronized
//    fun submitSelectiveRefreshLoadingData(refreshingList: List<T>? = null) {
//        selectivelyRefreshingItems = refreshingList
//        requestModelBuild()
//    }
//
//    fun snapshot() = pagedData
//
//    fun refreshingListSnapshot() = selectivelyRefreshingItems
//
//    abstract fun buildItemModels(item: T, index: Int, isItemRefreshing: Boolean)
//
//    protected open fun addHeaderModels(isDataEmpty: Boolean) {}
//
//    protected open fun addFooterModels(isDataEmpty: Boolean) {}
// }
