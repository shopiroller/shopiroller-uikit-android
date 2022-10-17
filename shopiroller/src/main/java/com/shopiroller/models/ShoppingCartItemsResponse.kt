package com.shopiroller.models

import com.google.gson.annotations.SerializedName

data class ShoppingCartItemsResponse(

	@field:SerializedName("shoppingCardCount")
	val shoppingCardCount: Int? = null
)
