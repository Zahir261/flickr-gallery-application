package com.zahir.flickrgalleryapplication.ui.filter

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zahir.flickrgalleryapplication.data.database.dao.TagDao
import com.zahir.flickrgalleryapplication.data.models.Language
import com.zahir.flickrgalleryapplication.data.models.SearchFilter
import com.zahir.flickrgalleryapplication.data.models.Tag
import com.zahir.flickrgalleryapplication.data.models.TagMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class FilterFragmentViewModel @Inject constructor(
    private val tagDao: TagDao
) : ViewModel() {

    // filter region
    private var _tagList = mutableListOf<String>()
    val tagList: List<String>
        get() = _tagList

    private var _tagMode: TagMode = TagMode.All
    val tagMode: TagMode
        get() = _tagMode

    private var _language: Language = Language.English
    val language: Language
        get() = _language
    // end filter region

    /**
     * Get all the tag from local database
     * Observe and update the auto complete list of TagView from fragment
     */
    private var _autoCompleteList = tagDao.getAllTags()
    val autoCompleteList: LiveData<List<Tag>>
        get() = _autoCompleteList

    /**
     * Initialize the filters with valus from [FilterBottomSheetFragment]
     *
     * @param searchFilter SearchFilter
     */
    fun init(searchFilter: SearchFilter) {
        _tagList.addAll(searchFilter.tags)
        _tagMode = searchFilter.tagMode
        _language = searchFilter.language
    }

    /**
     * Update the tag list
     * Add the newly added tag to local database
     *
     * @param tag String - newly added tag
     * @param tagList List<String> - updated tagList
     */
    fun onTagInserted(tag: String, tagList: List<String>) {
        updateTagList(tagList)
        viewModelScope.launch {
            tagDao.insertTag(Tag(0, tag, 1, Date(), Date()))
        }
    }

    /**
     * Update the tag list
     * Update selected tag's frequency and updated time to local database
     *
     * @param tag String - selected tag
     * @param tagList List<String> - updated tagList
     */
    fun onTagSelected(tag: String, tagList: List<String>) {
        updateTagList(tagList)
        viewModelScope.launch {
            tagDao.updateTag(tag, Date())
        }
    }

    /**
     * Update the tag list
     */
    fun onTagDeleted(tagList: List<String>) {
        updateTagList(tagList)
    }

    /**
     * Add the newly added tag to tag list
     * Add the newly added tag to local database
     *
     * @param tag String - newly added tag
     */
    fun addToTagList(tag: String) {
        _tagList.add(tag)
        viewModelScope.launch {
            tagDao.insertTag(Tag(0, tag, 1, Date(), Date()))
        }
    }

    /**
     * Set all the filter value to initial state
     */
    fun resetFilters() {
        _tagList.clear()
        _tagMode = TagMode.All
        _language = Language.English
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun updateTagList(tagList: List<String>) {
        _tagList = tagList as MutableList<String>
    }

    /**
     * Update the tag mode filter with selected tag
     *
     * @param tagMode String - selected tag
     */
    fun updateTagMode(tagMode: String) {
        _tagMode = TagMode.parse(tagMode)
    }

    /**
     * Update language filter with selected language
     *
     * @param language String - selected language
     */
    fun updateLanguage(language: String) {
        _language = Language.parse(language)
    }
}