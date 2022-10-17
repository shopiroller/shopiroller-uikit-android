package com.shopiroller.models.user;


import com.shopiroller.constants.Constants;
import com.shopiroller.util.validation.annotations.Required;

import java.io.Serializable;

public class BaseAddressModel implements Serializable {

    @Required
    public String id;

//    @LocalizedName(R.string.user_address_address_title)
    @Required
    public String title;

//    @LocalizedName(R.string.user_address_country)
    @Required
    public String country;

//    @LocalizedName(R.string.user_address_city)
    @Required
    public String state;

//    @LocalizedName(R.string.user_address_district)
    @Required
    public String city;

//    @LocalizedName(R.string.user_address_address_line)
    @Required
    public String addressLine;

//    @LocalizedName(R.string.user_address_postal_code)
    @Required
    public String zipCode;

    public String description;
    public boolean isDefault;
    @Required
    public AddressContactModel contact;

    public String getDescriptionArea()
    {
        return city
                + " / " + state + " / " + country
                + Constants.NEW_LINE
                + "" + contact.nameSurname
                + " - "
                + contact.phoneNumber;
    }


    public String getListDeliveryDescriptionArea()
    {
        return contact.nameSurname
                + Constants.NEW_LINE
                + addressLine
                + Constants.NEW_LINE
                + city + " / " + state + " / " + country
                + Constants.NEW_LINE
                + contact.phoneNumber;
    }


    public String getSummaryDescriptionArea()
    {
        return addressLine
                + " "
                + city
                + " / " + state
                + " " + contact.nameSurname
                + " - "
                + contact.phoneNumber;
    }


    public String getPopupAddressFirstLine()
    {
        return addressLine;
    }

    public String getPopupAddressSecondLine()
    {
        return city
                + " / " + state
                + " / " + country;
    }

    public String getPopupAddressThirdLine()
    {
        return contact.nameSurname
                + " - " + contact.phoneNumber;
    }

    public boolean isSelected;
}
