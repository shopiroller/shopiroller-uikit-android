package com.shopiroller.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AppliedCouponModel(
    var id: String? = null,
    var code: String? = null,
    var totalPrice: Double? = null,
    var currency: String? = null,
    var expireDate: String? = null,
    var type: String? = null,
    @SerializedName("value")
    var discountPrice: Double? = null
): Serializable
