package com.zahir.flickrgalleryapplication.ui.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.zahir.flickrgalleryapplication.data.models.ImageDetail
import com.zahir.flickrgalleryapplication.data.models.Media
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

class DetailsActivityViewModelTest {
    private lateinit var sut: DetailsActivityViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        sut = DetailsActivityViewModel()
    }

    @Test
    fun should_initialize_image_detail_when_called() {
        // Arrange
        val imageDetail = ImageDetail(
            title = "title",
            link = "www.abc.com",
            media = Media(m = "www.m.com"),
            dateTaken = Date(1000),
            description = "description",
            published = Date(1050),
            author = "author",
            authorId = "authorId",
            tags = "tags"
        )

        // Act
        sut.init(imageDetail)

        // Assert
        val actualImageDetail = sut.imageDetail.value
        assertEquals("title", actualImageDetail?.title)
        assertEquals("www.abc.com", actualImageDetail?.link)
        assertEquals("www.m.com", actualImageDetail?.media?.m)
        assertEquals(Date(1000), actualImageDetail?.dateTaken)
        assertEquals("description", actualImageDetail?.description)
        assertEquals(Date(1050), actualImageDetail?.published)
        assertEquals("author", actualImageDetail?.author)
        assertEquals("authorId", actualImageDetail?.authorId)
        assertEquals("tags", actualImageDetail?.tags)
    }

    @Test
    fun should_toggle_shouldShowDetails_when_called() {
        // Assert
        assertTrue(sut.shouldShowDetails.value!!)

        // Act
        sut.toggleDetailsVisibility()

        // Assert
        assertFalse(sut.shouldShowDetails.value!!)
    }
}