package com.zahir.flickrgalleryapplication.di

import android.content.Context
import com.zahir.flickrgalleryapplication.BuildConfig
import com.zahir.flickrgalleryapplication.data.api.ApiClient
import com.zahir.flickrgalleryapplication.utils.interceptors.NetworkConnectionInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.net.CookieManager
import java.util.concurrent.TimeUnit

/**
 * Module for providing network related dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    private val cookieJar: JavaNetCookieJar = JavaNetCookieJar(CookieManager())

    @Provides
    fun provideOkHttpClient(@ApplicationContext context: Context): OkHttpClient = OkHttpClient
        .Builder()
        .connectTimeout(120, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .addInterceptor {
            it.proceed(it.request().newBuilder().build())
        }
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .addInterceptor(NetworkConnectionInterceptor(context))
        .cookieJar(cookieJar)
        .build()

    @Provides
    fun provideApiClient(okHttpClient: OkHttpClient): ApiClient {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BuildConfig.FLICKR_BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(ApiClient::class.java)
    }
}