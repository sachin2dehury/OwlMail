package github.owlmail.core


/**
 * To check response success or failure
 */
sealed class ResponseState<T> {
    class Failure<T>(val message: String? = null, val value: T? = null): ResponseState<T>()
    class Success<T>(val value: T? = null) : ResponseState<T>()
    object Loading : ResponseState<Nothing>()
    object EmptyState : ResponseState<Nothing>()
}
