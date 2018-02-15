package com.example.hristiyan.menu.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.hristiyan.menu.daos.FoodDao;
import com.example.hristiyan.menu.daos.MenuDao;
import com.example.hristiyan.menu.data.Food;
import com.example.hristiyan.menu.data.Menu;

/**
 * Created by hristiyan on 08.02.18.
 */

@Database(entities = {Food.class, Menu.class}, version = 1)
public abstract class MenuDatabase extends RoomDatabase {

    public abstract MenuDao menuDao();

    public abstract FoodDao FoodDao();


}
