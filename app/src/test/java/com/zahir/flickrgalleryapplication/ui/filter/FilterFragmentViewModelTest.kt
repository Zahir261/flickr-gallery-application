package com.zahir.flickrgalleryapplication.ui.filter

import com.zahir.flickrgalleryapplication.data.models.Language
import com.zahir.flickrgalleryapplication.data.models.SearchFilter
import com.zahir.flickrgalleryapplication.data.models.TagMode
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FilterFragmentViewModelTest {
    private lateinit var sut: FilterFragmentViewModel

    @Before
    fun setUp() {
        sut = FilterFragmentViewModel()
    }

    @Test
    fun should_update_values_when_init_called() {
        // Arrange
        val searchFilter = SearchFilter(
            tags = listOf("Tag1", "Tag2", "Tag3"),
            tagMode = TagMode.Any,
            language = Language.German
        )

        // Act
        sut.init(searchFilter)

        // Assert
        assertEquals(listOf("Tag1", "Tag2", "Tag3"), sut.tagList)
        assertEquals(TagMode.Any, sut.tagMode)
        assertEquals(Language.German, sut.language)
    }

    @Test
    fun should_add_to_tag_list_when_addToTagList_called() {
        // Arrange
        val tag = "new tag"

        // Assert
        assertFalse(sut.tagList.contains(tag))

        // Act
        sut.addToTagList(tag)

        // Assert
        assertTrue(sut.tagList.contains(tag))
    }

    @Test
    fun should_remove_tag_from_list_when_onTagDeleted_called() {
        // Arrange
        val tag = "new tag"

        // Act
        sut.addToTagList(tag)

        // Assert
        assertTrue(sut.tagList.contains(tag))

        // Act
        sut.onTagDeleted(tag)

        // Assert
        assertFalse(sut.tagList.contains(tag))
    }

    @Test
    fun should_update_tag_mode_when_called() {
        // Arrange
        val tagMode = "Any tag"

        // Act
        sut.updateTagMode(tagMode)

        // Assert
        assertEquals(TagMode.Any, sut.tagMode)
    }

    @Test
    fun should_update_language_when_called() {
        // Arrange
        val language = "Italian"

        // Act
        sut.updateLanguage(language)

        // Assert
        assertEquals(Language.Italian, sut.language)
    }

    @Test
    fun should_reset_search_filter_when_called() {
        // Arrange
        val searchFilter = SearchFilter(
            tags = listOf("Tag1", "Tag2", "Tag3"),
            tagMode = TagMode.Any,
            language = Language.German
        )

        // Act
        sut.init(searchFilter)

        // Assert
        assertEquals(listOf("Tag1", "Tag2", "Tag3"), sut.tagList)
        assertEquals(TagMode.Any, sut.tagMode)
        assertEquals(Language.German, sut.language)

        // Act
        sut.resetFilters()

        // Assert
        assertEquals(emptyList<String>(), sut.tagList)
        assertEquals(TagMode.All, sut.tagMode)
        assertEquals(Language.English, sut.language)
    }
}