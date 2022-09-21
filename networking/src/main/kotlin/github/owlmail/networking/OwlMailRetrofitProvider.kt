package github.owlmail.networking

import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class OwlMailRetrofitProvider(
    private val moshi: Moshi,
    private val okHttpClient: OkHttpClient
) {
    private var owlMailApi: Retrofit? = null

    fun provideApi(): Retrofit = owlMailApi ?: Retrofit.Builder()
        .baseUrl("https://mail.nitrkl.ac.in/")
        .addConverterFactory(MoshiConverterFactory.create(moshi)).client(okHttpClient).build()
}