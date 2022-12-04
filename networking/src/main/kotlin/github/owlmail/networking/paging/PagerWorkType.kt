package github.owlmail.networking.paging

import androidx.annotation.IntDef

/**
 * Keeps the type of operation by the pager
 */

@IntDef(
    PagerWorkType.Refresh,
    PagerWorkType.Retry,
    PagerWorkType.SelectiveRefresh,
    PagerWorkType.Load
)
@Retention(AnnotationRetention.SOURCE)
annotation class PagerWorkType {
    companion object {
        const val Refresh = 0
        const val Retry = 1
        const val SelectiveRefresh = 2
        const val Load = 3
    }
}
