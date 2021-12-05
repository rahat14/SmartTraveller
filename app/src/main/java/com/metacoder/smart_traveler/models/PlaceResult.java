package com.metacoder.smart_traveler.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public  class PlaceResult {

    @Expose
    @SerializedName("vicinity")
    private String vicinity;
    @Expose
    @SerializedName("user_ratings_total")
    private int user_ratings_total;
    @Expose
    @SerializedName("scope")
    private String scope;
    @Expose
    @SerializedName("reference")
    private String reference;
    @Expose
    @SerializedName("rating")
    private double rating;
    @Expose
    @SerializedName("place_id")
    private String place_id;
    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("icon_mask_base_uri")
    private String icon_mask_base_uri;
    @Expose
    @SerializedName("icon_background_color")
    private String icon_background_color;
    @Expose
    @SerializedName("icon")
    private String icon;
    @Expose
    @SerializedName("geometry")
    private GeometryEntity geometry;
    @Expose
    @SerializedName("business_status")
    private String business_status;

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public int getUser_ratings_total() {
        return user_ratings_total;
    }

    public void setUser_ratings_total(int user_ratings_total) {
        this.user_ratings_total = user_ratings_total;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon_mask_base_uri() {
        return icon_mask_base_uri;
    }

    public void setIcon_mask_base_uri(String icon_mask_base_uri) {
        this.icon_mask_base_uri = icon_mask_base_uri;
    }

    public String getIcon_background_color() {
        return icon_background_color;
    }

    public void setIcon_background_color(String icon_background_color) {
        this.icon_background_color = icon_background_color;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public GeometryEntity getGeometry() {
        return geometry;
    }

    public void setGeometry(GeometryEntity geometry) {
        this.geometry = geometry;
    }

    public String getBusiness_status() {
        return business_status;
    }

    public void setBusiness_status(String business_status) {
        this.business_status = business_status;
    }

    public  class GeometryEntity {
        @Expose
        @SerializedName("location")
        private LocationEntity location;

        public LocationEntity getLocation() {
            return location;
        }

        public void setLocation(LocationEntity location) {
            this.location = location;
        }
    }

    public  class LocationEntity {
        @Expose
        @SerializedName("lng")
        private double lng;
        @Expose
        @SerializedName("lat")
        private double lat;

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }
    }
}
