package com.zahir.flickrgalleryapplication.utils

import android.view.View

/**
 * Show or hide view based on the flag.
 *If the flag is true, show the view, else hide.
 * @param shouldBeVisible Boolean
 */
fun View.toggleVisibleOrGone(shouldBeVisible: Boolean) {
    if (shouldBeVisible) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}