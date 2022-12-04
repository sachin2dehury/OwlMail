package github.owlmail.networking.paging

/**
 * Maps the network response to success or failure for page
 */

sealed interface PageResult<out Key : Any, out ItemType : Any, out ErrorType : Any> {
    data class Success<Key : Any, ItemType : Any>(
        val items: List<ItemType>? = null,
        val prevKey: Key? = null,
        val nextKey: Key? = null
    ) : PageResult<Key, ItemType, Nothing>

    data class Error<ErrorType : Any>(val error: ErrorType? = null) :
        PageResult<Nothing, Nothing, ErrorType>
}
