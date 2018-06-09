package com.debugps.gamesnews.roomTools.POJO;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.ArrayList;

@Entity(tableName = "user_table")
public class User {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String id;

    @ColumnInfo(name = "user")
    private String user;

    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "avatar")
    private String avatar;

    @ColumnInfo(name = "created_date")
    private String createdDate;

    public User(@NonNull String id, String user, String password, String avatar, String createdDate) {
        this.id = id;

        if(user == null){
            this.user = "";
        }else{
            this.user = user;
        }

        if(password == null){
            this.password = "";
        }else{
            this.password = password;
        }

        if(avatar == null){
            this.avatar = "";
        }else{
            this.avatar = avatar;
        }

        if(createdDate == null){
            this.createdDate = "";
        }else{
            this.createdDate = createdDate;
        }
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
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
