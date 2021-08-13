package com.zahir.flickrgalleryapplication.data.repositories.gallery

import com.squareup.moshi.Moshi
import com.zahir.flickrgalleryapplication.data.adapters.DateAdapter
import com.zahir.flickrgalleryapplication.data.api.ApiClient
import com.zahir.flickrgalleryapplication.data.models.FlickrImageAPIResponse
import com.zahir.flickrgalleryapplication.data.repositories.helper.RepositoryHelper
import com.zahir.flickrgalleryapplication.data.repositories.helper.ResultWrapper
import com.zahir.flickrgalleryapplication.utils.debugLogInfo
import java.util.*
import javax.inject.Inject

class GalleryRepository @Inject constructor(
    private val repositoryHelper: RepositoryHelper,
    private val apiClient: ApiClient
) {
    suspend fun getImageList(): ResultWrapper<FlickrImageAPIResponse> {
        return repositoryHelper.execute {
            parseResponse(apiClient.getPhotos()) ?: FlickrImageAPIResponse()
        }
    }

    /**
     * This method removes the extra part "jsonFlickrFeed(*) from response and convert the response
     * to [FlickrImageAPIResponse] object
     */
    private fun parseResponse(actualResponse: String): FlickrImageAPIResponse? {
        val parsableResponse = actualResponse.substring(0, actualResponse.length - 1).replace("jsonFlickrFeed(", "")
        debugLogInfo(parsableResponse)
        val moshi = Moshi.Builder().add(Date::class.java, DateAdapter()).build()
        val moshiAdapter = moshi.adapter(FlickrImageAPIResponse::class.java)
        return moshiAdapter.fromJson(parsableResponse)
    }
}