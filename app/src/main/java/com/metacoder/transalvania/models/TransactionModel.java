package com.metacoder.transalvania.models;

public class TransactionModel {
    long paid , purchase_time ;
    String trans_id , user_id ,  trip_id ;

    public TransactionModel() {
    }

    public TransactionModel(long paid, long purchase_time, String trans_id, String user_id, String trip_id) {
        this.paid = paid;
        this.purchase_time = purchase_time;
        this.trans_id = trans_id;
        this.user_id = user_id;
        this.trip_id = trip_id;
    }

    public long getPaid() {
        return paid;
    }

    public void setPaid(long paid) {
        this.paid = paid;
    }

    public long getPurchase_time() {
        return purchase_time;
    }

    public void setPurchase_time(long purchase_time) {
        this.purchase_time = purchase_time;
    }

    public String getTrans_id() {
        return trans_id;
    }

    public void setTrans_id(String trans_id) {
        this.trans_id = trans_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(String trip_id) {
        this.trip_id = trip_id;
    }
}
