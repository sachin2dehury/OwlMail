package github.owlmail.networking.paging

/**
 * Maps the network response to success or failure for selective refresh
 */

sealed interface SelectivePageResult<out ItemType : Any, out PayLoadType : Any, out ErrorType : Any> {
    data class Success<ItemType : Any>(val items: List<ItemType>? = null) :
        SelectivePageResult<ItemType, Nothing, Nothing>

    data class Error<ItemType : Any, PayLoadType : Any, ErrorType : Any>(
        val items: List<Pair<ItemType, PayLoadType>>,
        val error: ErrorType? = null
    ) : SelectivePageResult<ItemType, PayLoadType, ErrorType>
}
