package com.mobdeve.s16.group22.medelivery;

public class CartModel {

    private String cartItem;
    private int cartQuantity;
    private int cartPrice;

    // Default blank constructor for Firebase
    public CartModel() {
    }

    public CartModel(String cartItem, int cartQuantity, int cartPrice) {
        this.cartItem = cartItem;
        this.cartQuantity = cartQuantity;
        this.cartPrice = cartPrice;
    }

    public String getCartItem() {
        return cartItem;
    }

    public void setCartItem(String cartItem) {
        this.cartItem = cartItem;
    }

    public int getCartQuantity() {
        return cartQuantity;
    }

    public void setCartQuantity(int cartQuantity) {
        this.cartQuantity = cartQuantity;
    }

    public int getCartPrice() {
        return cartPrice;
    }

    public void setCartPrice(int cartPrice) {
        this.cartPrice = cartPrice;
    }
}
