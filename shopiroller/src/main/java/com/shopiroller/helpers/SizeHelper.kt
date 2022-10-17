package com.shopiroller.helpers

import android.content.Context
import android.util.TypedValue
import kotlin.math.roundToInt

object SizeHelper {
    @JvmStatic
    fun convertDpToPixel(dp: Float, context: Context): Int {
        val resources = context.resources
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).roundToInt()
    }

    @JvmStatic
    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId: Int = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }
}