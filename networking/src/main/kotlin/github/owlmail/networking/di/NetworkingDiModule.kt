package github.owlmail.networking.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import github.owlmail.networking.OwlMailAuthInterceptor
import github.owlmail.networking.OwlMailRetrofit
import github.owlmail.networking.provider.AuthInterceptorProvider
import github.owlmail.networking.provider.NetworkStateFlowProvider
import github.owlmail.networking.provider.RetrofitProvider
import javax.inject.Singleton
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkingDiModule {

    @Provides
    @Singleton
    internal fun provideNetworkStateFlow(@ApplicationContext context: Context) =
        NetworkStateFlowProvider(context)

    @Provides
    @Singleton
    internal fun providesChuckerInterceptor(@ApplicationContext context: Context) =
        ChuckerInterceptor.Builder(context).build()

//    @Provides
//    @Singleton
//    internal fun providesStetho(@ApplicationContext context: Context) =
//        Stetho.newInitializerBuilder(context).build()

    @Provides
    @Singleton
    internal fun providesAuthInterceptor(): OwlMailAuthInterceptor = AuthInterceptorProvider()

    @Provides
    @Singleton
    internal fun providesOkHttp(
        authInterceptor: OwlMailAuthInterceptor,
        chuckerInterceptor: ChuckerInterceptor
    ) = OkHttpClient.Builder().addInterceptor(authInterceptor)
        .addInterceptor(chuckerInterceptor).build()

    @Provides
    @Singleton
    internal fun providesMoshi() = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @Provides
    @Singleton
    internal fun providesMoshiConverterFactory(moshi: Moshi) = MoshiConverterFactory.create(moshi)

    @Provides
    @Singleton
    internal fun providesRetrofitBuilder(
        okHttpClient: OkHttpClient,
        moshiConverterFactory: MoshiConverterFactory
    ) = Retrofit.Builder()
        .client(okHttpClient)
        .addConverterFactory(moshiConverterFactory)

    @Provides
    @Singleton
    internal fun providesRetrofitProvider(retrofitBuilder: Retrofit.Builder): OwlMailRetrofit =
        RetrofitProvider(retrofitBuilder)
}
