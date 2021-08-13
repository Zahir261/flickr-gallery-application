package com.zahir.flickrgalleryapplication.data.models

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class FlickrImageAPIResponse(
    val items: List<ImageDetail>? = null
) : Parcelable
