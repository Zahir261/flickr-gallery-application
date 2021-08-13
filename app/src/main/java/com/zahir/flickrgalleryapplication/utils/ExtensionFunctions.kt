package com.zahir.flickrgalleryapplication.utils

import android.view.View

fun View.toggleVisibleOrGone(shouldBeVisible: Boolean) {
    if (shouldBeVisible) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}