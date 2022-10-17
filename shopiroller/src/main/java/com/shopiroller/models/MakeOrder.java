package com.shopiroller.models;

import com.google.gson.annotations.SerializedName;
import com.shopiroller.constants.Constants;
import com.shopiroller.models.user.UserBillingAddressModel;
import com.shopiroller.models.user.UserShippingAddressModel;

import java.io.Serializable;
import java.util.List;

public class MakeOrder implements Serializable {

    @SerializedName("userId")
    public String userId;
    @SerializedName("userNote")
    public String userNote;
    @SerializedName("bankAccount")
    public String bankAccount;
    @SerializedName("paymentAccount")
    public BankAccount paymentAccount;
    @SerializedName("paymentType")
    public String paymentType;
    @SerializedName("products")
    public List<OrderProductModel> products;
    @SerializedName("shippingAddress")
    public MakeOrderAddress shippingAddress;
    @SerializedName("billingAddress")
    public MakeOrderAddress billingAddress;
    @SerializedName("buyer")
    public BuyerOrderModel buyer;
    @SerializedName("creditCard")
    public OrderCard card;

    public double productPriceTotal;
    public double shippingPrice;
    public String currency;
    public String orderId;
    public boolean tryAgain;
    public BankAccount bankAccountModel;
    public UserShippingAddressModel userShippingAddressModel;
    public UserBillingAddressModel userBillingAddressModel;

    @Override
    public String toString() {
        return "MakeOrder{" +
                "userId='" + userId + '\'' +
                ", userNote='" + userNote + '\'' +
                ", bankAccount='" + bankAccount + '\'' +
                ", paymentType='" + paymentType + '\'' +
                ", products=" + products +
                ", shippingAddress=" + shippingAddress +
                ", billingAddress=" + billingAddress +
                ", buyer=" + buyer +
                ", card=" + card +
                ", productPriceTotal=" + productPriceTotal +
                ", shippingPrice=" + shippingPrice +
                ", currency='" + currency + '\'' +
                ", bankAccountModel=" + bankAccountModel +
                ", userShippingAddressModel=" + userShippingAddressModel +
                ", userBillingAddressModel=" + userBillingAddressModel +
                '}';
    }

    public CompleteOrder getCompleteOrderModel() {
        if (paymentType.equalsIgnoreCase(Constants.ONLINE3DS) || paymentType.equalsIgnoreCase(Constants.ONLINE))
            return new CompleteOrder(orderId, userNote, paymentType, card);
        else if (paymentType.equalsIgnoreCase(Constants.TRANSFER))
            return new CompleteOrder(orderId, userNote, paymentType, bankAccount, paymentAccount);
        else
            return new CompleteOrder(orderId, userNote, paymentType);
    }
}
