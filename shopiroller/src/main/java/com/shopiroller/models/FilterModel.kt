package com.shopiroller.models

import java.io.Serializable

data class FilterModel(
    var categoryIds: ArrayList<String?>? = null,
    var brandIds: ArrayList<String?>? = null,
    var variantData: String? = null,
    var minimumPrice: Double? = null,
    var maximumPrice: Double? = null,
    var stockSwitch: Boolean = false,
    var discountedProductsSwitch: Boolean = false,
    var freeShippingSwitch: Boolean = false

) : Serializable {

    fun getQueryMap(): MutableMap<String, Any> {
        val queryMap: MutableMap<String, Any> = mutableMapOf()
        if (!categoryIds.isNullOrEmpty())
            queryMap["categoryId"] = if (categoryIds!!.size > 1) categoryIds!!.toArray() else categoryIds!![0].toString()
        if (!brandIds.isNullOrEmpty())
            queryMap["brandId"] = if (brandIds!!.size > 1) brandIds!!.toArray() else brandIds!![0].toString()
        if (minimumPrice != null) queryMap["price.Min"] = minimumPrice.toString()
        if (maximumPrice != null) queryMap["price.Max"] = maximumPrice.toString()
        if (stockSwitch) queryMap["stock.Min"] = 1
        if (discountedProductsSwitch) queryMap["isThereCampaign"] = discountedProductsSwitch
        if (freeShippingSwitch) queryMap["freeShipping"] = freeShippingSwitch

        return queryMap
    }

    fun hasFilter(): Boolean {
        return (!categoryIds.isNullOrEmpty() || !brandIds.isNullOrEmpty() || !variantData.isNullOrEmpty() || minimumPrice != null || maximumPrice != null || stockSwitch || discountedProductsSwitch || freeShippingSwitch)
    }

}
