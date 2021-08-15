package com.zahir.flickrgalleryapplication.ui.gallery

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zahir.flickrgalleryapplication.data.models.FlickrImageAPIResponse
import com.zahir.flickrgalleryapplication.data.models.ImageDetail
import com.zahir.flickrgalleryapplication.data.models.SearchFilter
import com.zahir.flickrgalleryapplication.data.models.SearchParams
import com.zahir.flickrgalleryapplication.data.repositories.gallery.GalleryRepository
import com.zahir.flickrgalleryapplication.data.repositories.helper.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel associated with [GalleryActivity]
 */
@HiltViewModel
class GalleryActivityViewModel @Inject constructor(
    private val galleryRepository: GalleryRepository
) : ViewModel() {
    private var _imageList = MutableLiveData<List<ImageDetail>>()
    val imageList: LiveData<List<ImageDetail>>
        get() = _imageList

    private var _isNetworkCallSuccessful = MutableLiveData(true)
    val isNetworkCallSuccessful: LiveData<Boolean>
        get() = _isNetworkCallSuccessful

    private var _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?>
        get() = _errorMessage

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private var _sortOption: MutableLiveData<SortOption> = MutableLiveData(SortOption.ByDateTaken)

    private var _searchFilter = SearchFilter()
    val searchFilter: SearchFilter
        get() = _searchFilter

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val sortOption: LiveData<SortOption>
        get() = _sortOption

    /**
     * Call the api and images from API
     */
    fun getImages() {
        viewModelScope.launch {
            _isLoading.value = true
            val searchParams = prepareSearchParams()
            when (val response = galleryRepository.getImageList(
                searchParams.tags,
                searchParams.tagMode,
                searchParams.language
            )) {
                is ResultWrapper.Success<FlickrImageAPIResponse> -> {
                    _imageList.value = sortList(response.data.items, sortOption.value)
                    _isNetworkCallSuccessful.value = true
                    _errorMessage.value = null
                }
                is ResultWrapper.NetworkError -> {
                    _imageList.value = arrayListOf()
                    _isNetworkCallSuccessful.value = false
                    _errorMessage.value = "Please check your internet connection."
                }
                is ResultWrapper.GenericError -> {
                    _imageList.value = arrayListOf()
                    _isNetworkCallSuccessful.value = false
                    _errorMessage.value = "Sorry, something went wrong."
                }
            }
            _isLoading.value = false
        }
    }

    /**
     * Sort images by date taken or by date published
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun sortList(currentList: List<ImageDetail>?, sortOption: SortOption?): List<ImageDetail>? {
        return when (sortOption) {
            SortOption.ByDateTaken -> currentList?.sortedBy {
                it.dateTaken
            }
            SortOption.ByDatePublished -> currentList?.sortedBy {
                it.published
            }
            else -> currentList?.sortedBy {
                it.dateTaken
            }
        }
    }

    /**
     * Toggle sort options.
     * Change to by date taken if previously was by date publish and vice-versa
     */
    fun toggleSortOptions() {
        _sortOption.value = when (sortOption.value) {
            SortOption.ByDatePublished -> SortOption.ByDateTaken
            SortOption.ByDateTaken -> SortOption.ByDatePublished
            null -> SortOption.ByDateTaken
        }
        _imageList.value = sortList(imageList.value, sortOption.value)
    }

    /**
     * Update search filter with new value
     *
     * @param searchFilter new SearchFilter
     */
    fun updateSearchFilter(searchFilter: SearchFilter) {
        _searchFilter = searchFilter
    }

    /**
     * Prepare [SearchParams] from [SearchFilter]
     * Make a comma separated string from tag list
     *
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun prepareSearchParams(): SearchParams {
        val tags = if (searchFilter.tags.isEmpty()) {
            null
        } else {
            searchFilter.tags.joinToString(",")
        }
        val tagMode = searchFilter.tagMode.keyword
        val language = searchFilter.language.keyword
        return SearchParams(tags, tagMode, language)
    }
}

/**
 * Sealed class for holding the sort options like date taken and date published
 */
sealed class SortOption(val sortText: String) {
    object ByDateTaken : SortOption("By Date Taken")
    object ByDatePublished : SortOption("By Date Published")
}