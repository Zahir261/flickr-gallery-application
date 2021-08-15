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

    private var _autoCompleteList = tagDao.getAllTags()
    val autoCompleteList: LiveData<List<Tag>>
        get() = _autoCompleteList

    fun init(searchFilter: SearchFilter) {
        _tagList.addAll(searchFilter.tags)
        _tagMode = searchFilter.tagMode
        _language = searchFilter.language
    }

    fun onTagInserted(tag: String, tagList: List<String>) {
        updateTagList(tagList)
        viewModelScope.launch {
            tagDao.insertTag(Tag(0, tag, 1, Date(), Date()))
        }
    }

    fun onTagSelected(tag: String, tagList: List<String>) {
        updateTagList(tagList)
        viewModelScope.launch {
            tagDao.updateTag(tag, Date())
        }
    }

    fun onTagDeleted(tagList: List<String>) {
        updateTagList(tagList)
    }

    fun addToTagList(tag: String) {
        _tagList.add(tag)
        viewModelScope.launch {
            tagDao.insertTag(Tag(0, tag, 1, Date(), Date()))
        }
    }

    fun resetFilters() {
        _tagList.clear()
        _tagMode = TagMode.All
        _language = Language.English
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun updateTagList(tagList: List<String>) {
        _tagList = tagList as MutableList<String>
    }

    fun updateTagMode(tagMode: String) {
        _tagMode = TagMode.parse(tagMode)
    }

    fun updateLanguage(language: String) {
        _language = Language.parse(language)
    }
}