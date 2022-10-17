package com.shopiroller.models


import com.google.gson.annotations.SerializedName

data class ClientResponse(
    @SerializedName("accountName")
    val accountName: String?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("properties")
    val properties: Properties?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("type")
    val type: String?,
)