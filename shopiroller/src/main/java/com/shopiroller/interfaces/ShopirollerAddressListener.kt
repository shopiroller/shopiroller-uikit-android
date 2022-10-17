package com.shopiroller.interfaces

import com.shopiroller.models.user.UserBillingAddressModel
import com.shopiroller.models.user.UserShippingAddressModel

interface ShopirollerAddressListener {
    fun onNewBillingAddress(billingAddressModel: UserBillingAddressModel)
    fun onNewShippingAddress(shippingAddressModel: UserShippingAddressModel)
    fun onEditBillingAddress(billingAddressModel: UserBillingAddressModel)
    fun onEditShippingAddress(shippingAddressModel: UserShippingAddressModel)
}