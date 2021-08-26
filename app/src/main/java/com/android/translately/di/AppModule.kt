package com.android.translately.di

import com.android.translately.BuildConfig
import com.android.translately.network.LanguageApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideBaseUrl() = "https://google-translate1.p.rapidapi.com/language/"

    @Singleton
    @Provides
    @Named("Key")
    fun getKey() = "39d2241238msh53f7f18a81e38fcp13bd5ajsnfa0684ebc23e"

    @Singleton
    @Provides
    @Named("Host")
    fun getHost() = "google-translate1.p.rapidapi.com"


    @Provides
    @Singleton
    fun provideOkhttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    } else OkHttpClient
        .Builder()
        .build()

    open class Interceptors : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            var request = chain.request()
            request = request.newBuilder()
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Accept-Encoding", "application/gzip")
                .build()
            return chain.proceed(request)
        }
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        BASE_URL: String
    ): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun getApiService(retrofit: Retrofit): LanguageApi = retrofit.create(LanguageApi::class.java)
}