package com.zahir.flickrgalleryapplication.data.repositories.helper

import com.squareup.moshi.Moshi
import com.zahir.flickrgalleryapplication.data.api.interceptors.NoConnectivityException
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryHelper @Inject constructor() {
    /**
     * Execute the network call and return the result after wrapping based on the status of the API call
     */
    suspend fun <T> execute(retrievalAction: suspend () -> T): ResultWrapper<T> {
        return try {
            ResultWrapper.Success(retrievalAction())
        } catch (throwable: Throwable) {
            when (throwable) {
                is HttpException -> ResultWrapper.GenericError(
                    throwable.code(),
                    mapErrorResponse(throwable)
                )
                is NoConnectivityException -> ResultWrapper.NetworkError
                else -> ResultWrapper.GenericError()
            }
        }
    }

    private fun mapErrorResponse(throwable: HttpException): ErrorResponse? {
        return try {
            throwable.response()?.errorBody()?.let {
                Moshi.Builder().build().adapter(ErrorResponse::class.java)
                    .fromJson(it.charStream().toString())
            }
        } catch (exception: Exception) {
            null
        }
    }
}