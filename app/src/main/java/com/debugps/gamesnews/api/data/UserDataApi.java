package com.debugps.gamesnews.api.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UserDataApi {

    @SerializedName("_id")
    private String _id;

    @SerializedName("user")
    private String user;

    @SerializedName("password")
    private String password;

    @SerializedName("avatar")
    private String avatar;

    @SerializedName("created_date")
    private String createdDate;

    public UserDataApi(String _id, String user, String password, String avatar, String createdDate) {
        this._id = _id;
        this.user = user;
        this.password = password;
        this.avatar = avatar;
        this.createdDate = createdDate;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
