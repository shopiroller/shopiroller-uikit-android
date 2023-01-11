package com.shopiroller.models

data class VariantSelectionModel(
    var variationList: ArrayList<Variation>? = null,
    var variantGroupId: String? = null,
    var variantGroupName: String? = null,
    var variantGroupIsActive: Boolean = true,
)
