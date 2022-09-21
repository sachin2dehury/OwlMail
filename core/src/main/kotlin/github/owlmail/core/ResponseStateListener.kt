package github.owlmail.core


interface ResponseStateListener<T> {
    fun  ResponseState<T>.mapToState() = when (this) {
        is ResponseState.Success -> setSuccessState(this)
        is ResponseState.Failure -> setFailureState(this)
        ResponseState.Loading -> setLoadingState()
        ResponseState.EmptyState -> setEmptyState()
    }
    fun setSuccessState(responseState: ResponseState.Success<T>)
    fun setFailureState(responseState: ResponseState.Failure<T>)
    fun setEmptyState()
    fun setLoadingState()
}