package com.shopiroller.models.user;

import com.shopiroller.util.validation.annotations.Required;

import java.io.Serializable;

public class AddressContactModel implements Serializable {

//    @LocalizedName(R.string.user_address_name)
    @Required
    public String nameSurname;

//    @LocalizedName(R.string.user_address_phone)
    @Required
    public String phoneNumber;
    public String email;

    public static AddressContactModel getFakeContact() {
        AddressContactModel contactModel = new AddressContactModel();
        contactModel.email = "test@test.com";
        contactModel.phoneNumber = "05392476787";
        contactModel.nameSurname = "John Doe";
        return contactModel;
    }
}
