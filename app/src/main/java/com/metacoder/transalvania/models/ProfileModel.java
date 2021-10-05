package com.metacoder.transalvania.models;

public class ProfileModel {
    String name  , mail , user_id ;

    public ProfileModel() {
    }

    public ProfileModel(String name, String mail, String user_id) {
        this.name = name;
        this.mail = mail;
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
