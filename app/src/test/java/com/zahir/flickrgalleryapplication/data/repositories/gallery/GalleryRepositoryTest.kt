package com.zahir.flickrgalleryapplication.data.repositories.gallery

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.zahir.flickrgalleryapplication.data.api.ApiClient
import com.zahir.flickrgalleryapplication.data.models.FlickrImageAPIResponse
import com.zahir.flickrgalleryapplication.data.repositories.helper.RepositoryHelper
import com.zahir.flickrgalleryapplication.data.repositories.helper.ResultWrapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class GalleryRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var repositoryHelper: RepositoryHelper

    @Mock
    lateinit var apiClient: ApiClient

    private lateinit var sut: GalleryRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        repositoryHelper = RepositoryHelper()
        sut = GalleryRepository(repositoryHelper, apiClient)
    }

    @Test
    fun when_the_response_is_properly_formatted() = runBlockingTest {
        // Arrange
        val response = "jsonFlickrFeed({" +
                "\"title\": \"Uploads von allen\"," +
                "\"link\": \"https://www.flickr.com/photos/\"," +
                "\"description\": \"this is a description\"," +
                "\"modified\": \"2021-08-13T03:15:27Z\"," +
                "\"generator\": \"https://www.flickr.com\"," +
                "\"items\": [" +
                "{" +
                "\"title\": \"Nikki Galrani UHD stills\"," +
                "\"link\": \"https://www.flickr.com/photos/192112916@N05/51374001442\"," +
                "\"media\": {\"m\":\"https://live.staticflickr.com/65535/51374001442_88a8a7fb5a_m.jpg\"}," +
                "\"date_taken\": \"2021-04-24T23:34:29-08:00\"," +
                "\"description\": \"this is a description\"," +
                "\"published\": \"2021-08-13T03:15:27Z\"," +
                "\"author\": \"nobody@flickr.com\"," +
                "\"author_id\": \"192112916@N05\"," +
                "\"tags\": \"tamilactress\"" +
                "}]" +
                "})"

        Mockito.`when`(apiClient.getPhotos()).thenReturn(response)

        // Act
        val actualWrappedResponse = sut.getImageList(null, "all", "en-us")

        // Assert
        assertTrue(actualWrappedResponse is ResultWrapper.Success<FlickrImageAPIResponse>)
        assertEquals(1, (actualWrappedResponse as ResultWrapper.Success<FlickrImageAPIResponse>).data.items?.size)
    }

    @Test
    fun when_the_response_is_not_properly_formatted() = runBlockingTest {
        // Arrange
        val response = "jsonFlickr({" +
                "\"title\": \"Uploads von allen\"," +
                "\"link\": \"https://www.flickr.com/photos/\"," +
                "\"description\": \"this is a description\"," +
                "\"modified\": \"2021-08-13T03:15:27Z\"," +
                "\"generator\": \"https://www.flickr.com\"," +
                "\"items\": [" +
                "{" +
                "\"title\": \"Nikki Galrani UHD stills\"," +
                "\"link\": \"https://www.flickr.com/photos/192112916@N05/51374001442\"," +
                "\"media\": {\"m\":\"https://live.staticflickr.com/65535/51374001442_88a8a7fb5a_m.jpg\"}," +
                "\"date_taken\": \"2021-04-24T23:34:29-08:00\"," +
                "\"description\": \"this is a description\"," +
                "\"published\": \"2021-08-13T03:15:27Z\"," +
                "\"author\": \"nobody@flickr.com\"," +
                "\"author_id\": \"192112916@N05\"," +
                "\"tags\": \"tamilactress\"" +
                "}]" +
                "})"

        Mockito.`when`(apiClient.getPhotos()).thenReturn(response)

        // Act
        val actualWrappedResponse = sut.getImageList(null, "all", "en-us")

        // Assert
        assertTrue(actualWrappedResponse is ResultWrapper.GenericError)
    }
}