package com.zahir.flickrgalleryapplication.data.api

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiClient {
    @GET("photos_public.gne")
    suspend fun getPhotos(
        @Query("tags") tags: String? = null,
        @Query("tagmode") tagMode: String = "all",
        @Query("lang") language: String = "en-us",
        @Query("format") format: String = "json"
    ): String
}