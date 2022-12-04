package github.owlmail.networking.paging

/**
 * Maintains the page status for ui
 */

sealed interface PageStatus<out Key : Any, out ItemType : Any, out PayLoadType : Any, out ErrorType : Any> {
    /**
     * Initial State
     */
    object Default : PageStatus<Nothing, Nothing, Nothing, Nothing>

    /**
     * Loading States
     */
    sealed interface Loading : PageStatus<Nothing, Nothing, Nothing, Nothing> {
        object FirstPageLoading : Loading
        object NextPageLoading : Loading
        object PreviousPageLoading : Loading
    }

    sealed class Success<out Key : Any, out ItemType : Any>(
        open val offset: Key,
        @PagingDirection open val loadDirection: Int,
        open val data: List<ItemType>? = null
    ) : PageStatus<Key, ItemType, Nothing, Nothing> {

        /**
         * no data , you have to check if filter / search is applied
         */
        data class EmptyData<out Key : Any>(
            override val offset: Key,
            @PagingDirection override val loadDirection: Int
        ) : Success<Key, Nothing>(offset, loadDirection)

        /**
         * when page data is successfully loaded or refresh is done
         */
        data class PageLoaded<out Key : Any, out ItemType : Any>(
            override val offset: Key,
            @PagingDirection override val loadDirection: Int,
            override val data: List<ItemType>? = null
        ) : Success<Key, ItemType>(offset, loadDirection, data)

        /**
         * when no more data is there, it should also have the same impl as [PageLoaded]
         */
        data class NoMorePages<out Key : Any, out ItemType : Any>(
            override val offset: Key,
            @PagingDirection override val loadDirection: Int,
            override val data: List<ItemType>? = null
        ) : Success<Key, ItemType>(offset, loadDirection, data)
    }

    /**
     * Error thrown when page load is failed
     */
    data class Error<out Key : Any, out ErrorType : Any>(
        val offset: Key,
        @PagingDirection val loadDirection: Int,
        val error: ErrorType? = null
    ) : PageStatus<Key, Nothing, Nothing, ErrorType>

    sealed interface SelectiveRefresh<out ItemType : Any, out PayLoadType : Any, out ErrorType : Any> :
        PageStatus<Nothing, ItemType, PayLoadType, ErrorType> {
        /**
         * refreshing selective items
         */
        data class Loading<out ItemType : Any>(val items: List<ItemType>? = null) :
            SelectiveRefresh<ItemType, Nothing, Nothing>

        data class Success<out ItemType : Any, out PayLoadType : Any>(
            val request: List<Pair<ItemType, PayLoadType>>,
            val data: List<ItemType>? = null
        ) : SelectiveRefresh<ItemType, PayLoadType, Nothing>

        /**
         * Error thrown when selective refresh fails
         */
        data class Error<out ItemType : Any, out PayLoadType : Any, out ErrorType : Any>(
            val items: List<Pair<ItemType, PayLoadType>>,
            val error: ErrorType? = null
        ) : SelectiveRefresh<ItemType, PayLoadType, ErrorType>
    }
}
