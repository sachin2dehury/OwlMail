package github.owlmail.networking

sealed class NetworkState{
    object Available: NetworkState()
    object Unavailable: NetworkState()
    object Lost: NetworkState()
}
