package github.owlmail.networking

import github.owlmail.core.ResponseState
import retrofit2.Response


suspend inline fun <T> networkBoundResource(
    crossinline localLoading: suspend () -> T,
    crossinline remoteLoading: suspend () -> T,
    forRemoteLoading: Boolean = true
) = when (forRemoteLoading) {
    true -> remoteLoading()
    false -> localLoading()
}
fun <T> Response<T>.mapToResponseState() = when {
    isSuccessful && code() == 200 -> ResponseState.Success(body())
    else -> ResponseState.Failure(message())
}