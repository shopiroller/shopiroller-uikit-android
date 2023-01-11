package com.shopiroller.models

import com.google.gson.annotations.SerializedName

data class Variation(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("value")
    val value: String? = null,

    var isAvailable: Boolean? = false,

    var isSelected: Boolean? = false
)