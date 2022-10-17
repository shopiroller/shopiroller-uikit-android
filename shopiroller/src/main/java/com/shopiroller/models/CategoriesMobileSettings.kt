package com.shopiroller.models

import com.shopiroller.models.enums.CategoryDisplayTypeEnum
import java.io.Serializable

data class CategoriesMobileSettings (
    var categoryDisplayType: CategoryDisplayTypeEnum? = null
): Serializable
