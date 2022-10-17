package com.shopiroller.models;

import java.io.Serializable;

public class AddProductModel implements Serializable {
    public String productId;
    public int quantity;
    public String displayName;

    public AddProductModel(String productId, int quantity, String displayName) {
        this.productId = productId;
        this.quantity = quantity;
        if (displayName != null && displayName.length() > 200)
            this.displayName = displayName.substring(0, 200);
        else
            this.displayName = displayName;
    }
}
