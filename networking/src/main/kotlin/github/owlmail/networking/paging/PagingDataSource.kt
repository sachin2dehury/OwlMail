package github.owlmail.networking.paging

/**
 * Using Custom Paging due to 3 main reasons
 * 1. Issues with Selective Update
 * 2. This clears all the buildModel cache from Epoxy
 * 3. In Epoxy can't map one item to List of items
 *
 * A Generic Class that is a DataSource with various options
 */

interface PagingDataSource<Key : Any, ItemType : Any, PayLoadType : Any, out ErrorType : Any> {
    suspend fun loadDownward(offset: Key, loadSize: Int): PageResult<Key, ItemType, ErrorType> =
        PageResult.Success()

    suspend fun loadUpward(offset: Key, loadSize: Int): PageResult<Key, ItemType, ErrorType> =
        PageResult.Success()

    /**
     * Lets enforce this method to call any api & refresh item in the paged list
     */
    suspend fun selectivelyRefreshItems(itemsList: List<Pair<ItemType, PayLoadType>>): SelectivePageResult<ItemType, PayLoadType, ErrorType> =
        SelectivePageResult.Success()

    /**
     * To delete a item return null against the item in result
     */
    fun mapOldItemsToNewItems(
        oldItems: List<ItemType>,
        newItems: List<ItemType>?
    ): Map<ItemType, ItemType?> = emptyMap()
}
