package com.example.hristiyan.menu.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.hristiyan.menu.data.Food;

import java.util.List;
import java.util.UUID;

/**
 * Created by hristiyan on 08.02.18.
 */
@Dao
public interface FoodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFood(Food food);

    @Delete
    void deleteFood(Food food);

    @Query("Select * From Food Where menuId Like :menuId")
    List<Food> getFoodsForMenu(String menuId);

    @Update
    void updateFood(Food food);

    @Query("Select * From Food Where id Like :id")
    Food getFoodById(String id);
}
