package com.shopiroller.models;

import com.google.gson.annotations.SerializedName;
import com.shopiroller.Shopiroller;

import java.io.Serializable;

public class BuyerOrderModel implements Serializable {

    @SerializedName("name")
    public String name = "";
    @SerializedName("surname")
    public String surname = "";
    @SerializedName("email")
    public String email;

    public BuyerOrderModel() {
        email = Shopiroller.getUserEmail();
        String fullName = Shopiroller.getUserFullName();
        String[] names = fullName.split(" ");
        surname = names[names.length - 1];
        for (int i = 0; i < names.length - 1; i++) {
            if (i == 0)
                name += names[0];
            else {
                name += " " + names[i];
            }
        }
    }
}
