package github.owlmail.networking.state

sealed interface NetworkState {
    data object Available : NetworkState
    data object Unavailable : NetworkState

    operator fun invoke(
        availableState: (() -> Unit)? = null,
        unavailableState: (() -> Unit)? = null
    ) = when (this) {
        is Available -> availableState?.invoke()
        is Unavailable -> unavailableState?.invoke()
    }
}
