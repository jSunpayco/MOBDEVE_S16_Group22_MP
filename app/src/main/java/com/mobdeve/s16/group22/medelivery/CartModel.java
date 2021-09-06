package com.mobdeve.s16.group22.medelivery;

public class CartModel {

    private String cartName;
    private String cartQuantity;
    private String cartPrice;
    private String cartUid;

    // Default blank constructor for Firebase
    public CartModel() {
    }

    public CartModel(String cartName, String cartQuantity, String cartPrice, String cartUid) {
        this.cartName = cartName;
        this.cartQuantity = cartQuantity;
        this.cartPrice = cartPrice;
        this.cartUid = cartUid;
    }
    //


    public String getCartName() {
        return cartName;
    }

    public void setCartName(String cartName) {
        this.cartName = cartName;
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
