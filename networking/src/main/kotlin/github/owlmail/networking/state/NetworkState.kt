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

//    fun onAvailable(action: () -> Unit) = apply {
//        if (this is Available) action.invoke()
//    }
//
//    fun onUnavailable(action: () -> Unit) = apply {
//        if (this is Unavailable) action.invoke()
//    }
}
