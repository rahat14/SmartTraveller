package com.metacoder.transalvania.models;

import java.util.HashMap;
import java.util.Map;

public class postModel {
    public String postId, userId, postText, postMediaLink, isImage, userPicLink, userName;
    public long postTime, likeCount, commentCount;
    public Map<String, Boolean> likedUser = new HashMap<>();

    public String category;


    public postModel() {
    }

    public postModel(String postId, String userId, String postText, String postMediaLink,
                     String isImage, String userPicLink, String userName, long postTime,
                     long likeCount, long commentCount, Map<String, Boolean> likedUser, String category) {
        this.postId = postId;
        this.userId = userId;
        this.postText = postText;
        this.postMediaLink = postMediaLink;
        this.isImage = isImage;
        this.userPicLink = userPicLink;
        this.userName = userName;
        this.postTime = postTime;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.likedUser = likedUser;
        this.category = category;
    }
}
