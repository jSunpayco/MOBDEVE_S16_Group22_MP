package com.mobdeve.s16.group22.medelivery;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;

public class ItemsModel {

    private String itemName;
    private int itemPrice;
    private int itemQuantity;
    private String itemPath;

    // Default blank constructor for Firebase
    public ItemsModel() {

    }

    public ItemsModel(String itemName, int itemPrice, int itemQuantity, String itemPath) {
        this.itemPath = itemPath;
        this.itemQuantity = itemQuantity;
        this.itemPrice = itemPrice;
        this.itemName = itemName;
    }

    public String getItemName() {
        return itemName;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public String getItemPath() {
        return itemPath;
    }
}
