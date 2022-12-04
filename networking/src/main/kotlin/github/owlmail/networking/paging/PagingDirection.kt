package github.owlmail.networking.paging

import androidx.annotation.IntDef

/**
 * Defines the Direction to Load
 */

@Retention(AnnotationRetention.SOURCE)
@IntDef(PagingDirection.Upward, PagingDirection.Downward)
annotation class PagingDirection {
    companion object {
        const val Upward = -1
        const val Downward = 1
    }
}
