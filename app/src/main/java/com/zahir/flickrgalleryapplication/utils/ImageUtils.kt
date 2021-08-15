package com.zahir.flickrgalleryapplication.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

/**
 * Get bitmap from image and perform the action after getting the bitmap.
 *
 * Make sure to call this function on a background thread, else it will block main thread
 */
fun getBitmapFromUrlAndPerformAction(url: String, context: Context, afterAction: (Bitmap?) -> Unit) {
    GlideApp.with(context)
        .asBitmap()
        .load(url)
        .into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                afterAction(resource)
            }

            override fun onLoadCleared(placeholder: Drawable?) {
                afterAction(null)
            }

        })
}