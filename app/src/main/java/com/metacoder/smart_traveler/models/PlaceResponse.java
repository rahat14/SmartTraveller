package com.metacoder.smart_traveler.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlaceResponse {
    @Expose
    @SerializedName("results")
    private List<PlaceResult> placeResults;

    public List<PlaceResult> getPlaceResults() {
        return placeResults;
    }

    public void setPlaceResults(List<PlaceResult> placeResults) {
        this.placeResults = placeResults;
    }
}
