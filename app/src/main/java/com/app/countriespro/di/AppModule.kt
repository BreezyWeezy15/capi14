package com.app.countriespro.di

import com.app.countriespro.Utils
import com.app.countriespro.repositories.CountriesRepo
import com.app.countriespro.repositories.CountriesRepoImpl
import com.app.countriespro.services.AuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {


    @Singleton
    @Provides
    fun  provideHttLogger() : OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        val interceptor = httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofitInstance(okHttpClient: OkHttpClient) : AuthService {
        return  Retrofit.Builder()
            .baseUrl(Utils.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build().create(AuthService::class.java)
    }


     @Singleton
     @Provides
     fun provideRepoInstance(authService: AuthService): CountriesRepo {
         return CountriesRepo(authService)
     }
}