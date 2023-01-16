package com.shopiroller.models


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class VariantDataModel(
    @SerializedName("value")
    var value: String? = "",
    @SerializedName("variationGroupId")
    var variationGroupId: String? = "",
    @SerializedName("variationId")
    var variationId: String? = ""
) : Serializable
