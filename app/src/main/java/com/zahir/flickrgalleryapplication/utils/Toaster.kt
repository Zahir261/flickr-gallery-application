package com.zahir.flickrgalleryapplication.utils

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast

object Toaster {

    var toast: Toast? = null

    /**
     * Toast util to show toast message within the app.
     *
     * This dismisses previous toast (if available) and show next toast
     */
    @SuppressLint("ShowToast")
    fun showToast(context: Context, message: String, length: Int = Toast.LENGTH_SHORT) {
        dismissExistingToast()
        toast = Toast.makeText(context, message, length)
        toast?.show()
    }

    private fun dismissExistingToast() {
        if (toast != null) {
            toast?.cancel()
            toast = null
        }
    }
}