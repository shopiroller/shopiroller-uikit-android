package com.shopiroller.models

import com.google.gson.annotations.SerializedName

data class Variation(
    @SerializedName("id")
    val id: String,
    @SerializedName("value")
    val value: String,

    var isSelected: Boolean
)