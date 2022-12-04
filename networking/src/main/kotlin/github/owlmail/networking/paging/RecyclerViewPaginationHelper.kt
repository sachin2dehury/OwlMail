package github.owlmail.networking.paging

import androidx.recyclerview.widget.RecyclerView

/**
 * Helper function to attach [RecyclerViewPaginationListener] & removes on lifecycle destroy
 */

fun <Key : Any, ItemType : Any, PayloadType : Any, ErrorType : Any> RecyclerView.attachPaginationListener(
    pager: Pager<Key, ItemType, PayloadType, ErrorType>
) {
    val recyclerViewPaginationListener = RecyclerViewPaginationListener(pager)
    addOnScrollListener(recyclerViewPaginationListener)

    recyclerViewPaginationListener.triggerInitialLoad()

//    findViewTreeLifecycleOwner()?.lifecycle?.run {
//        addObserver(object : DefaultLifecycleObserver {
//            override fun onDestroy(owner: LifecycleOwner) {
//                removeObserver(this)
//                removeOnScrollListener(recyclerViewPaginationListener)
//                super.onDestroy(owner)
//            }
//        })
//    }
}
