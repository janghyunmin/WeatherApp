package com.kkjang.data.module.provider

import android.annotation.SuppressLint
import android.content.Context
import android.util.TypedValue
import androidx.annotation.StringRes

class ResourcesProvider(private val context: Context) {
    fun getString(@StringRes stringResId: Int): String = context.getString(stringResId)
    fun getString(@StringRes stringResId: Int, vararg formatArgs: Any?): String = context.getString(stringResId, formatArgs)
    fun getDeviceHeight(): Int = context.resources.displayMetrics.heightPixels
    fun getDeviceWidth(): Int = context.resources.displayMetrics.widthPixels

    @SuppressLint("InternalInsetResource")
    fun getStatusBarHeight(): Int {
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) { context.resources.getDimensionPixelSize(resourceId) }
        else 0
    }
    fun dpToPixel(dp: Int): Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics).toInt()
}