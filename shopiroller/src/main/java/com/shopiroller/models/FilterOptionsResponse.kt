package com.shopiroller.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class VariationsItem(

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("value")
    val value: String? = null,

    var isChecked: Boolean = false
) : Serializable

data class VariationGroupsItem(

    @field:SerializedName("updateDate")
    val updateDate: String? = null,

    @field:SerializedName("variations")
    val variations: List<VariationsItem?>? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("isActive")
    val isActive: Boolean? = null,

    @field:SerializedName("createDate")
    val createDate: String? = null,

    var selectedItems: String? = null
) : Serializable

data class FilterOptionsResponse(

    @field:SerializedName("variationGroups")
    val variationGroups: List<VariationGroupsItem?>? = null,

    @field:SerializedName("brands")
    val brands: List<BrandsItem?>? = null,

    @field:SerializedName("categories")
    val categories: List<CategoriesItem?>? = null
) : Serializable

data class BrandsItem(

    @field:SerializedName("updateDate")
    val updateDate: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("icon")
    val icon: ProductImage? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("isActive")
    val isActive: Boolean? = null,

    @field:SerializedName("createDate")
    val createDate: String? = null,

    var isChecked: Boolean = false
) : Serializable

data class CategoriesItem(

    @field:SerializedName("updateDate")
    val updateDate: String? = null,

    @field:SerializedName("name")
    val name: Map<String, String?>? = null,

    @field:SerializedName("icon")
    val icon: ProductImage? = null,

    @field:SerializedName("totalProduct")
    val totalProduct: Int? = null,

    @field:SerializedName("orderIndex")
    val orderIndex: Int? = null,

    @field:SerializedName("parentCategoryId")
    val parentCategoryId: String? = null,

    @field:SerializedName("isActive")
    val isActive: Boolean? = null,

    @field:SerializedName("categoryId")
    val categoryId: String? = null,

    @field:SerializedName("subCategories")
    val subCategories: List<CategoriesItem?>? = null,

    @field:SerializedName("createDate")
    val createDate: String? = null,

    var isSelected: Boolean = false
) : Serializable


data class MultiChoiceItem(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    var isChecked: Boolean = false
) : Serializable
