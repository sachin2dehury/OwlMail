package github.owlmail.networking

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull


abstract class OwlMailPagingSource<T : Any>(private val networkStateFlow: Flow<NetworkState>) :
    PagingSource<Int, T>() {
    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> = try {
        networkBoundResource(
            localLoading = { loadingPageFromLocal(params) },
            remoteLoading = { loadingPageFromRemote(params.key ?: 0, params.loadSize) },
            forRemoteLoading = networkStateFlow.firstOrNull() == NetworkState.Available
        )
    } catch (e: Exception) {
        LoadResult.Error(e)
    }

    abstract suspend fun loadingPageFromLocal(params: LoadParams<Int>): LoadResult<Int, T>
    abstract suspend fun loadingPageFromRemote(offset: Int, limit: Int): LoadResult<Int, T>
}