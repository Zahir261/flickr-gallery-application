package com.zahir.flickrgalleryapplication.data.models

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import java.util.*

/**
 * Model for items of Flickr Image API
 */
@JsonClass(generateAdapter = true)
@Parcelize
data class ImageDetail(
    val title: String? = null,
    val link: String? = null,
    val media: Media? = null,
    @Json(name = "date_taken")
    val dateTaken: Date? = null,
    val description: String? = null,
    val published: Date? = null,
    val author: String? = null,
    @Json(name = "author_id")
    val authorId: String? = null,
    val tags: String? = null
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class Media(
    val m: String? = null
) : Parcelable
