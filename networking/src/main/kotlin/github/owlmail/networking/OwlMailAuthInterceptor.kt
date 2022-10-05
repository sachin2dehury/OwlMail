package github.owlmail.networking

import okhttp3.Interceptor

interface OwlMailAuthInterceptor : Interceptor {
    operator fun invoke(authToken: String)
}
