package com.mobdeve.s16.group22.medelivery;


public class HistoryModel {
    private String date;
    private String transactionID;
    private String status;


    public HistoryModel(){

    }
    public HistoryModel(String date, String transactionID, String status){
        this.date = date;
        this.transactionID = transactionID;
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }
}
