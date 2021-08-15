package com.zahir.flickrgalleryapplication.ui.details

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zahir.flickrgalleryapplication.data.models.ImageDetail
import com.zahir.flickrgalleryapplication.utils.Toaster.showToast
import com.zahir.flickrgalleryapplication.utils.getBitmapFromUrlAndPerformAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import javax.inject.Inject

class DetailsActivityViewModel @Inject constructor() : ViewModel() {
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

    fun shareViaEmail(context: Context) {
        val imageUrl = imageDetail.value?.media?.m ?: return
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:") // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, "Image from FlickerGallery app")
        intent.putExtra(Intent.EXTRA_TEXT, "Check out this.\n$imageUrl")
        try {
            context.startActivity(
                Intent.createChooser(
                    intent,
                    "Choose Email Client..."
                )
            )
        } catch (ex: Exception) {
            CoroutineScope(Dispatchers.Main).launch {
                showToast(context, "Application not found")
            }
        }
    }

    fun saveToGallery(context: Context) {
        val imageUrl = imageDetail.value?.media?.m ?: return
        CoroutineScope(Dispatchers.IO).launch {
            getBitmapFromUrlAndPerformAction(imageUrl, context) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) saveImageInQ(
                    it,
                    context,
                    imageDetail.value?.title ?: "randomTitle"
                )
                else saveTheImageLegacyStyle(
                    it,
                    context,
                    imageDetail.value?.title ?: "randomTitle"
                )
            }
        }
    }

    //Make sure to call this function on a worker thread, else it will block main thread
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveImageInQ(bitmap: Bitmap?, context: Context, imageTitle: String) {
        if (bitmap == null) {
            CoroutineScope(Dispatchers.Main).launch {
                showToast(context, "Failed to save image")
            }
            return
        }
        val filename = "${imageTitle}.jpg"
        var fos: OutputStream?
        var imageUri: Uri?
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            put(MediaStore.Video.Media.IS_PENDING, 1)
        }

        //use application context to get contentResolver
        val contentResolver = context.contentResolver

        contentResolver.also { resolver ->
            imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            fos = imageUri?.let { resolver.openOutputStream(it) }
        }

        fos?.use { bitmap.compress(Bitmap.CompressFormat.JPEG, 80, it) }

        contentValues.clear()
        contentValues.put(MediaStore.Video.Media.IS_PENDING, 0)
        imageUri?.let { contentResolver.update(it, contentValues, null, null) }

        CoroutineScope(Dispatchers.Main).launch {
            showToast(context, "Image saved to gallery")
        }
    }

    //Make sure to call this function on a worker thread, else it will block main thread
    private fun saveTheImageLegacyStyle(bitmap: Bitmap?, context: Context, imageTitle: String) {
        if (bitmap == null) {
            CoroutineScope(Dispatchers.Main).launch {
                showToast(context, "Failed to save image")
            }
            return
        }
        val imagesDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val image = File(imagesDir, "${imageTitle}.jpg")
        val fos = FileOutputStream(image)

        fos.use { bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it) }

        CoroutineScope(Dispatchers.Main).launch {
            showToast(context, "Image saved to gallery")
        }
    }

    fun openToBrowser(context: Context) {
        imageDetail.value?.link?.let {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it)))
        }
    }

}