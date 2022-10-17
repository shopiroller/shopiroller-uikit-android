package com.shopiroller.models


import com.google.gson.annotations.SerializedName

data class VariationsModel(
    @SerializedName("id")
    val id: String,
    @SerializedName("value")
    val value: String
)