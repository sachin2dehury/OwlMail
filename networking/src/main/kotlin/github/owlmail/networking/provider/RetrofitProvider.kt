package github.owlmail.networking.provider

import github.owlmail.networking.OwlMailRetrofit
import retrofit2.Retrofit

internal class RetrofitProvider(private val retrofitBuilder: Retrofit.Builder) : OwlMailRetrofit {

    private lateinit var retrofit: Retrofit

    override fun invoke(baseUrl: String): Retrofit =
        retrofitBuilder.baseUrl(baseUrl).build().also { retrofit = it }

    override fun invoke() = retrofit
}
