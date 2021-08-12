package com.zahir.flickrgalleryapplication.data.repositories.helper

import com.zahir.flickrgalleryapplication.data.api.interceptors.NoConnectivityException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

@ExperimentalCoroutinesApi
class RepositoryHelperTest {
    lateinit var repositoryHelper: RepositoryHelper

    @Before
    fun setUp() {
        repositoryHelper = RepositoryHelper()
    }


    @Test
    fun when_api_call_is_successful() = runBlockingTest {
        // Arrange
        val response = "This is a test"

        // Act
        val result = repositoryHelper.execute {
            response
        }

        // Assert
        assertTrue(result is ResultWrapper.Success<String>)
        assertEquals("This is a test", (result as ResultWrapper.Success<String>).data)
    }

    @Test
    fun when_http_exception_occurs() = runBlockingTest {
        // Arrange
        val errorBody = "{\"errors\": [\"Unexpected parameter\"]}".toResponseBody("application/json".toMediaTypeOrNull())

        // Act
        val result = repositoryHelper.execute {
            throw HttpException(Response.error<Any>(400, errorBody))
        }

        // Assert
        assertTrue(result is ResultWrapper.GenericError)
        assertEquals(400, (result as ResultWrapper.GenericError).code)
        assertEquals(listOf("Unexpected parameter"), result.errorResponse?.errors)
    }

    @Test
    fun when_not_connected_to_internet() = runBlockingTest {
        // Act
        val result = repositoryHelper.execute {
            throw NoConnectivityException()
        }

        // Assert
        assertTrue(result is ResultWrapper.NetworkError)
    }

    @Test
    fun when_a_generic_error_occurs() = runBlockingTest {
        // Act
        val result = repositoryHelper.execute {
            throw RuntimeException()
        }

        // Assert
        assertTrue(result is ResultWrapper.GenericError)
        assertNull((result as ResultWrapper.GenericError).code)
        assertNull(result.errorResponse?.errors)
    }
}