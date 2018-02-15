package com.example.hristiyan.menu.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.hristiyan.menu.data.Menu;

import java.util.List;

/**
 * Created by hristiyan on 08.02.18.
 */
@Dao
public interface MenuDao {

    @Insert
    void insertMenu(Menu menu);

    @Update
    void updateMenu(Menu menu);

    @Delete
    void deleteMenu(Menu menu);

    @Query("Select * From Menu")
    List<Menu> getAllMenus();
}
