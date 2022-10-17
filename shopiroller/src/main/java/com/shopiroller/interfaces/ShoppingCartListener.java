package com.shopiroller.interfaces;

public interface ShoppingCartListener {

    void onClickRemoveItem(String shoppingCartItemId);

    void onClickUpdateQuantity(String shoppingCartItemId, int quantity);
}
