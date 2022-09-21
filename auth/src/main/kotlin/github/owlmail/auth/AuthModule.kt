package github.owlmail.auth

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import github.owlmail.auth.data.remote.AuthRepository
import github.owlmail.networking.OwlMailRetrofitProvider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    @Provides
    @Singleton
    fun providesAuthRepository(owlMailRetrofitProvider: OwlMailRetrofitProvider) =
        AuthRepository(owlMailRetrofitProvider)
}