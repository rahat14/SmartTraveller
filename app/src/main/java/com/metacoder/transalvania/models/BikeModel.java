package com.metacoder.transalvania.models;

import java.io.Serializable;

public class BikeModel implements Serializable {
    String id, address, admin_id, contact, image, name, rate , desc ;
      String current_rating ;
    public BikeModel() {
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public BikeModel(String id, String address, String admin_id, String contact, String image, String name, String rate, String desc, String current_rating) {
        this.id = id;
        this.address = address;
        this.admin_id = admin_id;
        this.contact = contact;
        this.image = image;
        this.name = name;
        this.rate = rate;
        this.desc = desc;
        this.current_rating = current_rating;
    }

    public String getCurrent_rating() {
        return current_rating;
    }

    public void setCurrent_rating(String current_rating) {
        this.current_rating = current_rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(String admin_id) {
        this.admin_id = admin_id;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
