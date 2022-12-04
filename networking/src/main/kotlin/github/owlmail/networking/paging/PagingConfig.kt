package github.owlmail.networking.paging

/**
 * Similar to [PagingConfig], provides default config to [Pager]
 */

data class PagingConfig<out Key : Any>(
    val initialOffSet: Key,
    val loadSize: Int = Default.loadSize,
    @PagingDirection
    val initialPagingDirection: Int = Default.initialPagingDirection,
    val initialLoadMultiplier: Int = Default.initialLoadMultiplier
) {
    object Default {
        const val loadSize = 10
        const val initialLoadMultiplier = 2

        @PagingDirection
        const val initialPagingDirection = PagingDirection.Downward
    }
}
