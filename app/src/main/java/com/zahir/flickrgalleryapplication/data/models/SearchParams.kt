package com.zahir.flickrgalleryapplication.data.models

/**
 * Image search API params
 */
data class SearchParams(
    val tags: String? = null,
    val tagMode: String = "all",
    val language: String = "en-us"
)