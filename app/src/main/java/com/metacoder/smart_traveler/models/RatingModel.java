package com.metacoder.smart_traveler.models;

import java.io.Serializable;

public class RatingModel implements Serializable {
    float rating;
    String user_id;
    String feedback ;

    public RatingModel(float rating, String user_id, String feedback) {
        this.rating = rating;
        this.user_id = user_id;
        this.feedback = feedback;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public RatingModel() {
    }


    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
