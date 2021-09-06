package com.mobdeve.s16.group22.medelivery;


public class OverviewItemModel {
    private String medicineName;
    private String quantity;
    private String price;

    public OverviewItemModel(){

    }
    public OverviewItemModel(String medicineName, String quantity, String price) {
        this.medicineName = medicineName;
        this.quantity = quantity;
        this.price = price;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}

