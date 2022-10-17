package com.shopiroller.models


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class VariationGroupsModel(
    @SerializedName("createDate")
    val createDate: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("isActive")
    val isActive: Boolean,
    @SerializedName("name")
    val name: String,
    @SerializedName("updateDate")
    val updateDate: String,
    @SerializedName("variations")
    val variations: List<Variation>
) : Serializable
