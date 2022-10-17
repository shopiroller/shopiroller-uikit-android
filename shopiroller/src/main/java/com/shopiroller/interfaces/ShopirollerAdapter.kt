package com.shopiroller.interfaces

interface ShopirollerAdapter {
    fun getAppKey(): String?
    fun getApiKey(): String?
    fun getAliasKey(): String?
    fun getDefaultLang(): String?
    fun getLocale(): String?
    fun getApplyzeBaseURL(): String?
    fun getShopirollerBaseURL(): String?
    fun getActionBarColor(): Int?
}