package com.shopiroller.models


import com.google.gson.annotations.SerializedName

data class Properties(
    @SerializedName("countryPhoneCode")
    val countryPhoneCode: String?,
    @SerializedName("accountNumber")
    val accountNumber: String?,
    @SerializedName("apiKey")
    val apiKey: String?,
    @SerializedName("eCommerceButton")
    val eCommerceButton: Boolean?,
    @SerializedName("isOnline")
    val isOnline: Boolean?,
    @SerializedName("whatsAppMobileIsActive")
    val whatsAppMobileIsActive: Boolean?,
    @SerializedName("whatsAppWebIsActive")
    val whatsAppWebIsActive: Boolean?
)