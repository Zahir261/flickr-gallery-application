package com.zahir.flickrgalleryapplication.ui.gallery

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.zahir.flickrgalleryapplication.data.models.FlickrImageAPIResponse
import com.zahir.flickrgalleryapplication.data.models.ImageDetail
import com.zahir.flickrgalleryapplication.data.repositories.gallery.GalleryRepository
import com.zahir.flickrgalleryapplication.data.repositories.helper.ResultWrapper
import com.zahir.flickrgalleryapplication.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class GalleryActivityViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var galleryRepository: GalleryRepository

    lateinit var sut: GalleryActivityViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        sut = GalleryActivityViewModel(galleryRepository)
    }

    @Test
    fun getImages_when_network_call_is_successful() = runBlockingTest {
        // Arrange
        val imageList = arrayListOf(
            ImageDetail(title = "title 1"),
            ImageDetail(title = "title 2"),
            ImageDetail(title = "title 3")
        )
        val response = FlickrImageAPIResponse(imageList)
        Mockito.`when`(galleryRepository.getImageList())
            .thenReturn(ResultWrapper.Success(response))

        // Act
        sut.getImages()

        // Assert
        assertFalse(sut.isLoading.getOrAwaitValue()!!)
        assertNull(sut.errorMessage.getOrAwaitValue())
        assertTrue(sut.isNetworkCallSuccessful.getOrAwaitValue()!!)
        assertNotNull(sut.imageList.getOrAwaitValue())
        assertEquals(3, sut.imageList.getOrAwaitValue()?.size)
    }

    @Test
    fun getImages_when_network_connection_occurs() = runBlockingTest {
        // Arrange
        Mockito.`when`(galleryRepository.getImageList())
            .thenReturn(ResultWrapper.NetworkError)

        // Act
        sut.getImages()

        // Assert
        assertFalse(sut.isLoading.getOrAwaitValue()!!)
        assertEquals("Please check your internet connection.", sut.errorMessage.getOrAwaitValue())
        assertFalse(sut.isNetworkCallSuccessful.getOrAwaitValue()!!)
        assertTrue(sut.imageList.getOrAwaitValue()?.isEmpty()!!)
    }

    @Test
    fun getImages_when_generic_error_occurs() = runBlockingTest {
        // Arrange
        Mockito.`when`(galleryRepository.getImageList())
            .thenReturn(ResultWrapper.GenericError())

        // Act
        sut.getImages()

        // Assert
        assertFalse(sut.isLoading.getOrAwaitValue()!!)
        assertEquals("Sorry, something went wrong.", sut.errorMessage.getOrAwaitValue())
        assertFalse(sut.isNetworkCallSuccessful.getOrAwaitValue()!!)
        assertTrue(sut.imageList.getOrAwaitValue()?.isEmpty()!!)
    }
}