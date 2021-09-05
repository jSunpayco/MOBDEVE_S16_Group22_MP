package com.mobdeve.s16.group22.medelivery;

import java.util.List;

public class CartModel {

    private String cartItem;
    private String cartQuantity;
    private String cartPrice;
    private String cartUid;

    // Default blank constructor for Firebase
    public CartModel() {
    }

    public CartModel(String cartItem, String cartQuantity, String cartPrice, String cartUid) {
        this.cartItem = cartItem;
        this.cartItem = cartQuantity;
        this.cartItem = cartPrice;
        this.cartItem = cartUid;
    }
    //


    public String getCartItem() {
        return cartItem;
    }

    public void setCartItem(String cartItem) {
        this.cartItem = cartItem;
    }

    public String getCartQuantity() {
        return cartQuantity;
    }

    public void setCartQuantity(String cartQuantity) {
        this.cartQuantity = cartQuantity;
    }

    public String getCartPrice() {
        return cartPrice;
    }

    public void setCartPrice(String cartPrice) {
        this.cartPrice = cartPrice;
    }

    public String getCartUid() {
        return cartUid;
    }

    public void setCartUid(String cartUid) {
        this.cartUid = cartUid;
    }

}
