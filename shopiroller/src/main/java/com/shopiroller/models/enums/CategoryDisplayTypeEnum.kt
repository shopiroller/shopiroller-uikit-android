package com.shopiroller.models.enums

import com.google.gson.annotations.SerializedName

enum class CategoryDisplayTypeEnum (val value: String) {

    @SerializedName("ImageAndText")
    IMAGE_AND_TEXT("ImageAndText"),

    @SerializedName("TextOnly")
    TEXT_ONLY("TextOnly"),

    @SerializedName("ImageOnly")
    IMAGE_ONLY("ImageOnly")
}