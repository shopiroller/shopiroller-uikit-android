package com.shopiroller.models;

import com.google.gson.annotations.SerializedName;
import com.shopiroller.constants.Constants;

import java.io.Serializable;

public class MakeOrderAddress implements Serializable {

    @SerializedName("id")
    public String id;

    @SerializedName("city")
    public String city;

    @SerializedName("country")
    public String country;

    @SerializedName("state")
    public String state;

    @SerializedName("description")
    public String description;

    @SerializedName("zipCode")
    public String zipCode;

    @SerializedName("identityNumber")
    public String identityNumber;

    @SerializedName("companyName")
    public String companyName;

    @SerializedName("taxNumber")
    public String taxNumber;

    @SerializedName("phoneNumber")
    public String phoneNumber;

    @SerializedName("taxOffice")
    public String taxOffice;

    @SerializedName("nameSurname")
    public String nameSurname;

    public String getTextArea() {
        String address = description + " ";
        if (companyName != null) {
            address += companyName + " - ";
        }
        address += city + "/" + state
                + Constants.NEW_LINE;

        return address;
    }

    public String getDescriptionArea() {
        return description
                + Constants.NEW_LINE
                + city
                + " / " + state + " / " + country
                + Constants.NEW_LINE
                + "%s"
                + " - "
                + phoneNumber;
    }

    public String getBillingDescriptionArea() {
        if (taxOffice != null && !taxOffice.equalsIgnoreCase(""))
            return description
                    + Constants.NEW_LINE
                    + city
                    + " / " + state + " / " + country
                    + Constants.NEW_LINE
                    + "%s"
                            + " - "
                            + phoneNumber
                    + Constants.NEW_LINE
                    + taxOffice + " - " + taxNumber;
        else
                return description
                    + Constants.NEW_LINE
                    + city
                    + " / " + state + " / " + country
                    + Constants.NEW_LINE
                    + "%s"
                            + " - "
                            + phoneNumber
                    + Constants.NEW_LINE
                    + identityNumber;

}

    @Override
    public String toString() {
        return "MakeOrderAddress{" +
                "id='" + id + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", state='" + state + '\'' +
                ", description='" + description + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", identityNumber='" + identityNumber + '\'' +
                ", companyName='" + companyName + '\'' +
                ", taxNumber='" + taxNumber + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", taxOffice='" + taxOffice + '\'' +
                '}';
    }
}
