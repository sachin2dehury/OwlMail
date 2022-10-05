package github.owlmail.core.state

sealed interface ResultState<out T> {
    data class Success<out T>(val data: T? = null) : ResultState<T>
    data class Error<out T>(val exception: Exception, val data: T? = null) : ResultState<T>
    data object Loading : ResultState<Nothing>
    data object Empty : ResultState<Nothing>

    operator fun invoke(
        emptyState: (() -> Unit)? = null,
        loadingState: (() -> Unit)? = null,
        successState: ((T?) -> Unit)? = null,
        errorState: ((Exception, T?) -> Unit)? = null
    ) = when (this) {
        is Empty -> emptyState?.invoke()
        is Loading -> loadingState?.invoke()
        is Success -> successState?.invoke(data)
        is Error -> errorState?.invoke(exception, data)
    }

//    fun onEmpty(action: () -> Unit) = apply {
//        if (this is Empty) action.invoke()
//    }
//
//    fun onLoading(action: () -> Unit) = apply {
//        if (this is Loading) action.invoke()
//    }
//
//    fun onSuccess(action: (T?) -> Unit) = apply {
//        if (this is Success) action.invoke(data)
//    }
//
//    fun onError(action: (Exception, T?) -> Unit) = apply {
//        if (this is Error) action.invoke(exception, data)
//    }
}
