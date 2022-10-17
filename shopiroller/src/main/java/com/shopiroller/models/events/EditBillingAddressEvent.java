package com.shopiroller.models.events;


import com.shopiroller.models.user.UserBillingAddressModel;

import java.io.Serializable;

public class EditBillingAddressEvent implements Serializable {

    public UserBillingAddressModel addressModel;

    public EditBillingAddressEvent(UserBillingAddressModel addressModel) {
        this.addressModel = addressModel;
    }
}
