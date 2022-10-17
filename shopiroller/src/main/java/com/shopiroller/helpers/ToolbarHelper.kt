package com.shopiroller.helpers

import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.shopiroller.Shopiroller
import com.shopiroller.util.ColorUtil

class ToolbarHelper {
    private val statusBarColorPermission: Int
        private get() {
            val color = Color.parseColor("#517fff")
            val a = Color.alpha(color)
            val r = Math.round(Color.red(color) * 0.9f)
            val g = Math.round(Color.green(color) * 0.9f)
            val b = Math.round(Color.blue(color) * 0.9f)
            return Color.argb(a,
                    Math.min(r, 255),
                    Math.min(g, 255),
                    Math.min(b, 255))
        }

    fun setStatusBar(appCompatActivity: AppCompatActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = appCompatActivity.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = statusBarColor
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decor = appCompatActivity.window.decorView
            if ((if (ColorUtil.isColorDark(Shopiroller.getTheme().primaryColor)) Color.WHITE else Color.BLACK) == Color.WHITE) {
                var flags = decor.systemUiVisibility
                flags = flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                decor.systemUiVisibility = flags
            } else {
                decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }

    fun setStatusBarColor(appCompatActivity: AppCompatActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = appCompatActivity.window
            window.statusBarColor = statusBarColor
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decor = appCompatActivity.window.decorView
            if ((if (ColorUtil.isColorDark(Shopiroller.getTheme().primaryColor)) Color.WHITE else Color.BLACK) == Color.WHITE) {
                var flags = decor.systemUiVisibility
                flags = flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                decor.systemUiVisibility = flags
            } else {
                decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }

    fun setStatusBarTransparent(appCompatActivity: AppCompatActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            appCompatActivity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

    companion object {
        val statusBarColor: Int
            get() {
                val color = Shopiroller.getTheme().primaryColor
                val a = Color.alpha(color)
                val r = Math.round(Color.red(color) * 0.9f)
                val g = Math.round(Color.green(color) * 0.9f)
                val b = Math.round(Color.blue(color) * 0.9f)
                return Color.argb(a,
                        Math.min(r, 255),
                        Math.min(g, 255),
                        Math.min(b, 255))
            }
    }
}