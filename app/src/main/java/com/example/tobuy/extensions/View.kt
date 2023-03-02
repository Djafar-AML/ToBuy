package com.example.tobuy.extensions

import android.view.View
import androidx.annotation.ColorInt
import com.google.android.material.color.MaterialColors


@ColorInt
fun View.getAttrColor(attrResId: Int): Int {
    return MaterialColors.getColor(this, attrResId)
}
