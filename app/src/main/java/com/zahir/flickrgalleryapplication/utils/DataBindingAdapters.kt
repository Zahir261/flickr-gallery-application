package com.zahir.flickrgalleryapplication.utils

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.zahir.flickrgalleryapplication.R

@BindingAdapter("app:imageUrl")
fun setImageUrl(imageView: ImageView, url: String?) {
    url?.let {
        val requestOptions = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .centerCrop()
            .placeholder(R.drawable.ic_placeholder)
            .error(R.drawable.ic_failed)

        Glide.with(imageView.context)
            .load(it)
            .apply(requestOptions)
            .transition(withCrossFade())
            .into(imageView)
    }
}

@BindingAdapter("app:shouldBeVisible")
fun shouldBeVisible(view: View, shouldBeVisible: Boolean) {
    view.toggleVisibleOrGone(shouldBeVisible)
}