package com.metacoder.transalvania.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LocationModel implements Serializable {

    String id, name, desc, trip_duration , current_rating , queryText;
    long trip_cost;
    String mainImage;

    public LocationModel() {
    }

    public LocationModel(String id, String name, String desc, String trip_duration, String current_rating, String queryText, long trip_cost, String mainImage) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.trip_duration = trip_duration;
        this.current_rating = current_rating;
        this.queryText = queryText;
        this.trip_cost = trip_cost;
        this.mainImage = mainImage;
    }

    public String getQueryText() {
        return queryText;
    }

    public void setQueryText(String queryText) {
        this.queryText = queryText;
    }

    public String getCurrent_rating() {
        return current_rating;
    }

    public void setCurrent_rating(String current_rating) {
        this.current_rating = current_rating;
    }

    public String getTrip_duration() {
        return trip_duration;
    }

    public void setTrip_duration(String trip_duration) {
        this.trip_duration = trip_duration;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }


    public long getTrip_cost() {
        return trip_cost;
    }

    public void setTrip_cost(long trip_cost) {
        this.trip_cost = trip_cost;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
