package com.shopiroller.uikit.sample

import android.app.Application
import com.shopiroller.interfaces.ShopirollerAdapter

class SampleApplication: Application(),ShopirollerAdapter {

    companion object {
        var shopirollerAdapter: ShopirollerAdapter? = null
    }

    override fun onCreate() {
        super.onCreate()
        shopirollerAdapter = this
    }

    // Shopiroller integration
    override fun getAppKey(): String {
        return ""
    }

    override fun getApiKey(): String {
        return ""
    }

    override fun getAliasKey(): String? {
        return ""
    }

    override fun getDefaultLang(): String {
        return ""
    }

    override fun getLocale(): String {
        return ""
    }

    override fun getApplyzeBaseURL(): String {
        return ""
    }

    override fun getShopirollerBaseURL(): String {
        return ""
    }

    override fun getActionBarColor(): Int {
        return 0
    }

}