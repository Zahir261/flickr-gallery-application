package com.zahir.flickrgalleryapplication.ui.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zahir.flickrgalleryapplication.data.models.FlickrImageAPIResponse
import com.zahir.flickrgalleryapplication.data.models.ImageDetail
import com.zahir.flickrgalleryapplication.data.repositories.gallery.GalleryRepository
import com.zahir.flickrgalleryapplication.data.repositories.helper.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryActivityViewModel @Inject constructor(
    private val galleryRepository: GalleryRepository
) : ViewModel() {
    private var _imageList = MutableLiveData<ArrayList<ImageDetail>>()
    val imageList: LiveData<ArrayList<ImageDetail>>
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

    fun getImages() {
        viewModelScope.launch {
            _isLoading.value = true
            when (val response = galleryRepository.getImageList()) {
                is ResultWrapper.Success<FlickrImageAPIResponse> -> {
                    _imageList.value = response.data.items as ArrayList<ImageDetail>?
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
}