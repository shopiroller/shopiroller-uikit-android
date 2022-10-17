package com.shopiroller.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class ShowcaseModel(

    @field:SerializedName("publishmentDate")
    val publishmentDate: String? = null,

    @field:SerializedName("updateDate")
    val updateDate: String? = null,

    @field:SerializedName("isPublished")
    val isPublished: Boolean? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("orderIndex")
    val orderIndex: Int? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("createDate")
    val createDate: String? = null,

    @field:SerializedName("status")
    val status: ShowcaseStatus? = null
) : Serializable, Parcelable

enum class ShowcaseStatus {
    Active,
    Passive,
    Planned
}
