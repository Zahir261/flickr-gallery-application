package com.zahir.flickrgalleryapplication.utils

import android.os.Build
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.zahir.flickrgalleryapplication.R

@BindingAdapter("app:imageUrl")
fun setImageUrl(imageView: ImageView, url: String?) {
    url?.let {
        val requestOptions = RequestOptions()
            .placeholder(R.drawable.ic_placeholder)
            .error(R.drawable.ic_failed)

        GlideApp.with(imageView.context)
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

@BindingAdapter("app:htmlText")
fun setHtmlText(textView: TextView, text: String?) {
    textView.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT)
    } else {
        Html.fromHtml(text)
    }
}