package com.shopiroller.models.user;



import java.io.Serializable;

public class DefaultAddressList implements Serializable {

//    @LocalizedName(value = R.string.user_delivery_address)
    public UserShippingAddressModel shippingAddress;

//    @SuppressLint("NonConstantResourceId")
//    @LocalizedName(R.string.user_billing_address)
    public UserBillingAddressModel billingAddress;


}
