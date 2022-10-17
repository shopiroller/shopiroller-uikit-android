package com.shopiroller

import android.content.Context
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.shopiroller.interfaces.ShopirollerAdapter
import com.shopiroller.interfaces.ShopirollerAddressListener
import com.shopiroller.interfaces.ShopirollerListener

object Shopiroller {

    @JvmStatic
    @NonNull
    var adapter: ShopirollerAdapter? = null

    @JvmStatic
    var listener: ShopirollerListener? = null

    @JvmStatic
    var addressListener: ShopirollerAddressListener? = null

    //todo set field
    @JvmStatic
    @NonNull
    var userLoginStatus: Boolean = false
        set(value) {
            field = value
            if (!value) {
                userId = null
                userEmail = null
                userFullName = null
            }
        }

    @JvmStatic
    @Nullable
    var userId: String? = null

    @JvmStatic
    @Nullable
    var userEmail: String? = null

    @JvmStatic
    @Nullable
    var userFullName: String? = null

    @JvmStatic
    fun getTheme(): Theme {
        return Theme()
    }

    fun init(adapter: ShopirollerAdapter, context: Context) {
        this.adapter = adapter
    }
}