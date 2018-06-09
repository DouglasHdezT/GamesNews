package com.debugps.gamesnews.roomTools.POJO;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.v4.content.PermissionChecker;

@Entity(tableName = "favorite_list_table")
public class FavoriteList {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String id;

    public FavoriteList(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }
}
