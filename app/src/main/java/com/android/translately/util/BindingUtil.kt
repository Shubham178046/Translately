package com.android.translately.util

import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.android.translately.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target


object BindingUtil {

    @JvmStatic
    @BindingAdapter("languageImage")
    fun loadImage(view: AppCompatImageView, resources: Int) {
        Glide.with(view.context)
            .load(resources).apply(RequestOptions().circleCrop())
            .into(view)
    }
}