package com.shopiroller.models;

import java.io.Serializable;

public class UpdateCartItemQuantity implements Serializable {

    int amount;

    public UpdateCartItemQuantity(int amount) {
        this.amount = amount;
    }
}
