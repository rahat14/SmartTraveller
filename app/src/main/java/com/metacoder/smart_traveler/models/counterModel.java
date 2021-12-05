package com.metacoder.smart_traveler.models;

public class counterModel {
    public long postCounter, likeCounter, commentCounter;
    public String id;

    public counterModel() {
    }

    public counterModel(long postCounter, long likeCounter, long commentCounter, String id) {
        this.postCounter = postCounter;
        this.likeCounter = likeCounter;
        this.commentCounter = commentCounter;
        this.id = id;
    }
}
