package com.zahir.flickrgalleryapplication.data.models

data class SearchParams(
    val tags: String? = null,
    val tagMode: String = "all",
    val language: String = "en-us"
)