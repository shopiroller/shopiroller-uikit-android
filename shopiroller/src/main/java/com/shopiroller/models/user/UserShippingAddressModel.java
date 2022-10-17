package com.shopiroller.models.user;


public class UserShippingAddressModel extends BaseAddressModel {

    public static UserShippingAddressModel getFakeAddres() {
        UserShippingAddressModel fakeAddress = new UserShippingAddressModel();
        fakeAddress.city = "Antalya";
        fakeAddress.state = "Türkiye";
        fakeAddress.country = "Türkiye";
        fakeAddress.description = "Adres";
        fakeAddress.id = "id";
        fakeAddress.zipCode = "07700";
        fakeAddress.contact = AddressContactModel.getFakeContact();
        return fakeAddress;
    }

}
