package github.owlmail.networking.provider

import github.owlmail.networking.OwlMailAuthInterceptor
import okhttp3.Interceptor
import okhttp3.Response

internal class AuthInterceptorProvider : OwlMailAuthInterceptor {

    private var authToken: String? = null

    override fun invoke(authToken: String) {
        this.authToken = authToken
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val tokenAbsent = request.header(Cookie).isNullOrEmpty() && !authToken.isNullOrEmpty()
        if (tokenAbsent) {
            request = request.newBuilder().addHeader(Cookie, TokenFormat + authToken).build()
        }
        return chain.proceed(request)
    }

    private companion object {
        private const val Cookie = "Cookie"
        private const val TokenFormat = "ZM_AUTH_TOKEN="
    }
}
