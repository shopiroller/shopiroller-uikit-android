package com.shopiroller.models


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class VariantDataModel(
    @SerializedName("value")
    val value: String,
    @SerializedName("variationGroupId")
    val variationGroupId: String,
    @SerializedName("variationId")
    val variationId: String
) : Serializable
