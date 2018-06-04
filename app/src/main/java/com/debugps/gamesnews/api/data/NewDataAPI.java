package com.debugps.gamesnews.api.data;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * POJO interpretado por GSON en las peticiones a la API
 */
public class NewDataAPI {

    @SerializedName("_id")
    @NonNull
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("body")
    private String body;

    @SerializedName("game")
    private String game;

    @SerializedName("description")
    private String description;

    @SerializedName("coverImage")
    private String coverImage;

    @SerializedName("created_date")
    private String createdDate;

    @SerializedName("__v")
    private int __v;

    public NewDataAPI(@NonNull String id, String title, String body, String game, String description, String coverImage, String createdDate, int v) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.game = game;
        this.description = description;
        this.coverImage = coverImage;
        this.createdDate = createdDate;
        __v = v;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public int get__v() {
        return __v;
    }

    public void set__v(int __v) {
        this.__v = __v;
    }
}
