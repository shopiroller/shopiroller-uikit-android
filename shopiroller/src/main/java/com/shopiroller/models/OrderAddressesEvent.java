package com.shopiroller.models;


import com.shopiroller.models.user.UserBillingAddressModel;
import com.shopiroller.models.user.UserShippingAddressModel;

import java.io.Serializable;

public class OrderAddressesEvent implements Serializable {
    public UserBillingAddressModel userBillingAddressModel;
    public UserShippingAddressModel userShippingAddressModel;

    public OrderAddressesEvent(UserShippingAddressModel userShippingAddressModel, UserBillingAddressModel userBillingAddressModel) {
        this.userBillingAddressModel = userBillingAddressModel;
        this.userShippingAddressModel = userShippingAddressModel;
    }
}
