package com.shopiroller.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OrderDetailModel extends Order {

    @SerializedName("bankAccount")
    public String bankAccount = "";
    @SerializedName("paymentAccount")
    public BankAccount paymentAccount ;
    @SerializedName("shippingAddress")
    public MakeOrderAddress shippingAddress = new MakeOrderAddress();
    @SerializedName("billingAddress")
    public MakeOrderAddress billingAddress = new MakeOrderAddress();
    @SerializedName("buyer")
    public Buyer buyer = new Buyer();
    @SerializedName("appliedCoupon")
    public AppliedCouponModel appliedCouponModel = new AppliedCouponModel();


    public class Buyer implements Serializable {

        @SerializedName("name")
        public String name = "";
        @SerializedName("surname")
        public String surname = "";

        public String getFullName()
        {
            return name + surname;
        }
    }


}
