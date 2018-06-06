package com.debugps.gamesnews.api.data;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class PlayerDataApi {


    @NonNull
    @SerializedName("_id")
    private String _id;

    @SerializedName("name")
    private String name;

    @SerializedName("avatar")
    private String avatar;

    @SerializedName("biografia")
    private String biografia;

    @SerializedName("game")
    private String game;

    public PlayerDataApi(@NonNull String _id, String name, String avatar, String biografia, String game) {
        this._id = _id;
        this.name = name;
        this.avatar = avatar;
        this.biografia = biografia;
        this.game = game;
    }

    @NonNull
    public String get_id() {
        return _id;
    }

    public void set_id(@NonNull String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBiografia() {
        return biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }
}
