package com.shopiroller.models

import java.io.Serializable

data class CategoriesWithOptionsModel (
    var categories: List<CategoryResponseModel>? = null,
    var mobileSettings: CategoriesMobileSettings? = null
): Serializable
