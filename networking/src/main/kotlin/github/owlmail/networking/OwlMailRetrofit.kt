package github.owlmail.networking

import retrofit2.Retrofit

interface OwlMailRetrofit {
    operator fun invoke(): Retrofit
    operator fun invoke(baseUrl: String): Retrofit
}
