package com.metacoder.transalvania.models;

import java.io.Serializable;

public class EventModel implements Serializable {

    private int stock;
    private String priceDetails;
    private String placeDetails;
    private String name;
    private int max_ppl;
    private int id;
    private String banner_image;
    private String location;

    public EventModel() {
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public EventModel(int stock, String priceDetails, String placeDetails, String name, int max_ppl, int id, String banner_image, String location) {
        this.stock = stock;
        this.priceDetails = priceDetails;
        this.placeDetails = placeDetails;
        this.name = name;
        this.max_ppl = max_ppl;
        this.id = id;
        this.banner_image = banner_image;
        this.location = location;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getPriceDetails() {
        return priceDetails;
    }

    public void setPriceDetails(String priceDetails) {
        this.priceDetails = priceDetails;
    }

    public String getPlaceDetails() {
        return placeDetails;
    }

    public void setPlaceDetails(String placeDetails) {
        this.placeDetails = placeDetails;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMax_ppl() {
        return max_ppl;
    }

    public void setMax_ppl(int max_ppl) {
        this.max_ppl = max_ppl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBanner_image() {
        return banner_image;
    }

    public void setBanner_image(String banner_image) {
        this.banner_image = banner_image;
    }
}
