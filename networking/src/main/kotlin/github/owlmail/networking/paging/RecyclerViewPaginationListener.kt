package github.owlmail.networking.paging

import androidx.recyclerview.widget.RecyclerView

/**
 * A [RecyclerView.OnScrollListener] to trigger pagination
 */

class RecyclerViewPaginationListener<in Key : Any, ItemType : Any, PayloadType : Any, out ErrorType : Any>(
    private val pager: Pager<Key, ItemType, PayloadType, ErrorType>
) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        triggerLoad(recyclerView, dy)
    }

    fun triggerInitialLoad() {
        pager.loadInitial()
    }

    private fun triggerLoad(recyclerView: RecyclerView, dy: Int) {
        when {
            !recyclerView.canScrollVertically(PagingDirection.Downward) && dy >= 0 -> {
                pager.load(PagingDirection.Downward)
            }
            !recyclerView.canScrollVertically(PagingDirection.Upward) && dy < 0 -> {
                pager.load(PagingDirection.Upward)
            }
            else -> {
                // no-op
            }
        }
    }
}
