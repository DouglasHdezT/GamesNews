package com.debugps.gamesnews.roomTools.DAO;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.debugps.gamesnews.roomTools.POJO.Category;

import java.util.List;

@Dao
public interface CategoryDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCategory(Category category);

    @Query("DELETE FROM category_table")
    void deleteAllCategories();

    @Query("SELECT * FROM category_table ORDER BY category ASC")
    LiveData<List<Category>> getAllCategories();

    @Query("SELECT count(*) FROM category_table")
    LiveData<Double> getCantCategories();
}
