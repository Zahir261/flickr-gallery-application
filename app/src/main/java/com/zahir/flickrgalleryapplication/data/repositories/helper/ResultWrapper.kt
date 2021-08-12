package com.zahir.flickrgalleryapplication.data.repositories.helper

/**
 * A wrapper class for API response.
 */
sealed class ResultWrapper<out T> {
    /**
     * If the API call is successful, it will be wrapped as Success<T>
     */
    data class Success<out T>(val data: T) : ResultWrapper<T>()

    /**
     * If there is an error in the API call
     */
    data class GenericError(val code: Int? = null, val errorResponse: ErrorResponse? = null) :
        ResultWrapper<Nothing>()

    /**
     * If the device is not connected to internet
     */
    object NetworkError : ResultWrapper<Nothing>()
}

data class ErrorResponse(
    val errors: List<String>
)