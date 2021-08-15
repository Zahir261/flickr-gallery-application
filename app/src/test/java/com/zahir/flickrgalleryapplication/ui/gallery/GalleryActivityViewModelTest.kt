package com.zahir.flickrgalleryapplication.ui.gallery

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.zahir.flickrgalleryapplication.data.models.FlickrImageAPIResponse
import com.zahir.flickrgalleryapplication.data.models.ImageDetail
import com.zahir.flickrgalleryapplication.data.models.Language
import com.zahir.flickrgalleryapplication.data.models.SearchFilter
import com.zahir.flickrgalleryapplication.data.models.TagMode
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
import java.util.*

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
        Mockito.`when`(
            galleryRepository.getImageList(
                Mockito.any(),
                Mockito.anyString(),
                Mockito.anyString()
            )
        )
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
        Mockito.`when`(
            galleryRepository.getImageList(
                Mockito.any(),
                Mockito.anyString(),
                Mockito.anyString()
            )
        )
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
        Mockito.`when`(
            galleryRepository.getImageList(
                Mockito.any(),
                Mockito.anyString(),
                Mockito.anyString()
            )
        )
            .thenReturn(ResultWrapper.GenericError())

        // Act
        sut.getImages()

        // Assert
        assertFalse(sut.isLoading.getOrAwaitValue()!!)
        assertEquals("Sorry, something went wrong.", sut.errorMessage.getOrAwaitValue())
        assertFalse(sut.isNetworkCallSuccessful.getOrAwaitValue()!!)
        assertTrue(sut.imageList.getOrAwaitValue()?.isEmpty()!!)
    }

    @Test
    fun test_sortList_by_date_taken() {
        // Arrange
        val firstImage = ImageDetail(dateTaken = Date(1000))
        val secondImage = ImageDetail(dateTaken = Date(1001))
        val thirdImage = ImageDetail(dateTaken = Date(999))
        val fourthImage = ImageDetail(dateTaken = Date(1005))
        val fifthImage = ImageDetail(dateTaken = Date(1003))
        val imageList = listOf(firstImage, secondImage, thirdImage, fourthImage, fifthImage)

        // Act
        val sortedList = sut.sortList(imageList, SortOption.ByDateTaken)

        // Assert
        assertEquals(
            listOf(thirdImage, firstImage, secondImage, fifthImage, fourthImage),
            sortedList
        )
    }

    @Test
    fun test_sortList_by_date_published() {
        // Arrange
        val firstImage = ImageDetail(published = Date(1000))
        val secondImage = ImageDetail(published = Date(1001))
        val thirdImage = ImageDetail(published = Date(999))
        val fourthImage = ImageDetail(published = Date(1005))
        val fifthImage = ImageDetail(published = Date(1003))
        val imageList = listOf(firstImage, secondImage, thirdImage, fourthImage, fifthImage)

        // Act
        val sortedList = sut.sortList(imageList, SortOption.ByDatePublished)

        // Assert
        assertEquals(
            listOf(thirdImage, firstImage, secondImage, fifthImage, fourthImage),
            sortedList
        )
    }

    @Test
    fun changeSortOption_when_by_date_taken_should_change_to_by_date_published() = runBlockingTest {
        // Arrange
        val firstImage = ImageDetail(dateTaken = Date(1000), published = Date(600))
        val secondImage = ImageDetail(dateTaken = Date(1001), published = Date(603))
        val thirdImage = ImageDetail(dateTaken = Date(999), published = Date(599))
        val fourthImage = ImageDetail(dateTaken = Date(1005), published = Date(1000))
        val fifthImage = ImageDetail(dateTaken = Date(1003), published = Date(300))
        val imageList = listOf(firstImage, secondImage, thirdImage, fourthImage, fifthImage)
        Mockito.`when`(
            galleryRepository.getImageList(
                Mockito.any(),
                Mockito.anyString(),
                Mockito.anyString()
            )
        )
            .thenReturn(ResultWrapper.Success(FlickrImageAPIResponse(imageList)))

        // Act
        sut.getImages()

        // Assert
        assertTrue(sut.sortOption.value is SortOption.ByDateTaken)
        assertEquals(
            listOf(thirdImage, firstImage, secondImage, fifthImage, fourthImage),
            sut.imageList.value
        )

        // Act
        sut.toggleSortOptions()

        // Assert
        assertTrue(sut.sortOption.value is SortOption.ByDatePublished)
        assertEquals(
            listOf(fifthImage, thirdImage, firstImage, secondImage, fourthImage),
            sut.imageList.value
        )
    }

    @Test
    fun changeSortOption_when_by_date_published_should_change_to_by_date_taken() = runBlockingTest {
        // Arrange
        val firstImage = ImageDetail(dateTaken = Date(1000), published = Date(600))
        val secondImage = ImageDetail(dateTaken = Date(1001), published = Date(603))
        val thirdImage = ImageDetail(dateTaken = Date(999), published = Date(599))
        val fourthImage = ImageDetail(dateTaken = Date(1005), published = Date(1000))
        val fifthImage = ImageDetail(dateTaken = Date(1003), published = Date(300))
        val imageList = listOf(firstImage, secondImage, thirdImage, fourthImage, fifthImage)
        Mockito.`when`(
            galleryRepository.getImageList(
                Mockito.any(),
                Mockito.anyString(),
                Mockito.anyString()
            )
        )
            .thenReturn(ResultWrapper.Success(FlickrImageAPIResponse(imageList)))

        // Act
        sut.getImages()
        sut.toggleSortOptions()

        //Assert
        assertTrue(sut.sortOption.value is SortOption.ByDatePublished)
        assertEquals(
            listOf(fifthImage, thirdImage, firstImage, secondImage, fourthImage),
            sut.imageList.value
        )

        // Act
        sut.toggleSortOptions()

        // Assert
        assertTrue(sut.sortOption.value is SortOption.ByDateTaken)
        assertEquals(
            listOf(thirdImage, firstImage, secondImage, fifthImage, fourthImage),
            sut.imageList.value
        )
    }

    @Test
    fun updateSearchFilter_should_update_search_filter_when_called() {
        // Arrange
        val searchFilter = SearchFilter(
            tags = listOf("Tag1", "Tag2", "Tag3"),
            tagMode = TagMode.Any,
            language = Language.German
        )

        // Act
        sut.updateSearchFilter(searchFilter)

        // Assert
        assertEquals(listOf("Tag1", "Tag2", "Tag3"), sut.searchFilter.tags)
        assertEquals(TagMode.Any, sut.searchFilter.tagMode)
        assertEquals(Language.German, sut.searchFilter.language)
    }

    @Test
    fun prepareParams_test_search_parameters() {
        // Arrange
        val searchFilter = SearchFilter(
            tags = listOf("Tag1", "Tag2", "Tag3"),
            tagMode = TagMode.Any,
            language = Language.German
        )

        // Act
        sut.updateSearchFilter(searchFilter)
        val actualParams = sut.prepareSearchParams()

        // Assert
        assertEquals("Tag1,Tag2,Tag3", actualParams.tags)
        assertEquals("any", actualParams.tagMode)
        assertEquals("de-de", actualParams.language)
    }
}