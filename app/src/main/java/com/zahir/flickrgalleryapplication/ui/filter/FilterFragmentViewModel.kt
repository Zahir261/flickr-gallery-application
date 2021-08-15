package com.zahir.flickrgalleryapplication.ui.filter

import androidx.lifecycle.ViewModel
import com.zahir.flickrgalleryapplication.data.models.Language
import com.zahir.flickrgalleryapplication.data.models.SearchFilter
import com.zahir.flickrgalleryapplication.data.models.TagMode
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FilterFragmentViewModel @Inject constructor() : ViewModel() {

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

    fun init(searchFilter: SearchFilter) {
        _tagList.addAll(searchFilter.tags)
        _tagMode = searchFilter.tagMode
        _language = searchFilter.language
    }

    fun onTagInserted(tag: String) {
        addToTagList(tag)
    }

    fun onTagSelected(tag: String) {
        addToTagList(tag)
    }

    fun onTagDeleted(tag: String) {
        _tagList.remove(tag)
    }

    fun resetFilters() {
        _tagList.clear()
        _tagMode = TagMode.All
        _language = Language.English
    }

    fun addToTagList(tag: String) {
        _tagList.add(tag)
    }

    fun updateTagMode(tagMode: String) {
        _tagMode = TagMode.parse(tagMode)
    }

    fun updateLanguage(language: String) {
        _language = Language.parse(language)
    }
}