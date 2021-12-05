package com.metacoder.smart_traveler.models;

import java.io.Serializable;

public class CalacModel implements Serializable {
    String desc, price;

    public CalacModel() {
    }

    public CalacModel(String desc, String price) {
        this.desc = desc;
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
