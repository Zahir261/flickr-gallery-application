package com.zahir.flickrgalleryapplication.utils.interceptors

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Check network connection before sending the request.
 *
 * If not connected to internet, throw [NoConnectivityException]
 */
class NetworkConnectionInterceptor(@ApplicationContext private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isConnected()) {
            throw NoConnectivityException()
        }
        val builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }

    private fun isConnected(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val networkInfo = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                networkInfo.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                networkInfo.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                networkInfo.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            return networkInfo.isConnected
        }
    }
}

class NoConnectivityException : IOException() {
    override val message: String
        get() = "No internet connection"
}