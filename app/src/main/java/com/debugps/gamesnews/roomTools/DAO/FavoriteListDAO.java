package com.debugps.gamesnews.roomTools.DAO;

import android.app.UiAutomation;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.debugps.gamesnews.roomTools.POJO.FavoriteList;

import java.util.List;

@Dao
public interface FavoriteListDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavNew(FavoriteList id);

    @Query("DELETE FROM favorite_list_table WHERE id = :id")
    void deleteFavNew(String id);

    @Query("DELETE FROM favorite_list_table")
    void deleteAllFavs();

    @Query("SELECT * FROM favorite_list_table")
    LiveData<List<FavoriteList>> getAllFavNews();

}
