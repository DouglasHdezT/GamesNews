package com.debugps.gamesnews.roomTools.POJO;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "player_table")
public class Player {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "_id")
    private String _id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "avatar")
    private String avatar;

    @ColumnInfo(name = "biografia")
    private String biografia;

    @ColumnInfo(name = "game")
    private String game;

    public Player(@NonNull String _id, String name, String avatar, String biografia, String game) {
        this._id = _id;

        if(name ==  null){
            this.name = "";
        }else{
            this.name = name;
        }

        if(avatar ==  null){
            this.avatar = "";
        }else{
            this.avatar = avatar;
        }

        if(biografia ==  null){
            this.biografia = "";
        }else{
            this.biografia = biografia;
        }

        if(game ==  null){
            this.game= "";
        }else{
            this.game = game;
        }
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
