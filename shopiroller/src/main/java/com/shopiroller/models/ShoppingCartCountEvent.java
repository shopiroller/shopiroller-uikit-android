package com.shopiroller.models;

public class ShoppingCartCountEvent {
    public int shoppingCartItemCount;

    public ShoppingCartCountEvent(int shoppingCartItemCount) {
        this.shoppingCartItemCount = shoppingCartItemCount;
    }
}
