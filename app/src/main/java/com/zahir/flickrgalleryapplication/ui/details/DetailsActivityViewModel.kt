package com.zahir.flickrgalleryapplication.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zahir.flickrgalleryapplication.data.models.ImageDetail

class DetailsActivityViewModel : ViewModel() {
    private var _imageDetail = MutableLiveData<ImageDetail>()
    val imageDetail: LiveData<ImageDetail>
        get() = _imageDetail

    private var _shouldShowDetails = MutableLiveData(true)
    val shouldShowDetails: LiveData<Boolean>
        get() = _shouldShowDetails

    fun init(imageDetail: ImageDetail?) {
        _imageDetail.value = imageDetail
    }

    fun toggleDetailsVisibility() {
        _shouldShowDetails.value = !shouldShowDetails.value!!
    }
}