package com.shopiroller.models.user;


import com.shopiroller.constants.Constants;
import com.shopiroller.util.validation.annotations.Required;

public class UserBillingAddressModel extends BaseAddressModel {

    @Required
    public String type;
    public String identityNumber;
    public String companyName;
    public String taxNumber;
    public String taxOffice;

    public static UserBillingAddressModel getFakeAddres() {
        UserBillingAddressModel fakeAddress = new UserBillingAddressModel();
        fakeAddress.city = "Antalya";
        fakeAddress.companyName = "Mobiroller";
        fakeAddress.state = "Türkiye";
        fakeAddress.country = "Türkiye";
        fakeAddress.description = "Adres";
        fakeAddress.id = "id";
        fakeAddress.identityNumber = "1234567890";
        fakeAddress.taxNumber = "12341234";
        fakeAddress.zipCode = "07700";
        fakeAddress.contact = AddressContactModel.getFakeContact();
        return fakeAddress;
    }


    public String getListBillingDescriptionArea()
    {
        if(type.equalsIgnoreCase("Corporate") )
            return contact.nameSurname
                    + Constants.NEW_LINE
                    + addressLine
                    + Constants.NEW_LINE
                    + city + " / " + state + " / " + country
                    + Constants.NEW_LINE
                    + contact.phoneNumber
                    + Constants.NEW_LINE
                    + taxOffice + " - " + taxNumber ;
        else
            return contact.nameSurname
                    + Constants.NEW_LINE
                    + addressLine
                    + Constants.NEW_LINE
                    + city + " / " + state + " / " + country
                    + Constants.NEW_LINE
                    + contact.phoneNumber
                    + Constants.NEW_LINE
                    + identityNumber;

    }

}
