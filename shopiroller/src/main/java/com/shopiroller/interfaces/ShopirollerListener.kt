package com.shopiroller.interfaces

import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.shopiroller.models.user.DefaultAddressList
import com.shopiroller.models.user.UserBillingAddressModel
import com.shopiroller.models.user.UserShippingAddressModel

interface ShopirollerListener {
    fun onBadgeCountChanged(@NonNull count: Int)
    fun loginNeeded()
    fun onStartShoppingClicked()
    fun onDefaultAddressChanged(billingAddressId: String, shippingAddressId: String)
    fun onNewShippingAddressClicked(activity: FragmentActivity)
    fun onNewBillingAddressClicked(activity: FragmentActivity)
    fun onEditShippingAddressClicked(activity: FragmentActivity, shippingAddress: UserShippingAddressModel)
    fun onEditBillingAddressClicked(activity: FragmentActivity, billingAddressModel: UserBillingAddressModel)
    fun getDefaultAddresses(listener: ShopirollerCallBackListener<DefaultAddressList>)
    fun getBillingAddresses(listener: ShopirollerCallBackListener<List<UserBillingAddressModel>>)
    fun getShippingAddresses(listener: ShopirollerCallBackListener<List<UserShippingAddressModel>>)
}