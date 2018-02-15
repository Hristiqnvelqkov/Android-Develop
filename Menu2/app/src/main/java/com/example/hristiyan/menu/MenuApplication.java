package com.example.hristiyan.menu;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.provider.ContactsContract;

import com.example.hristiyan.menu.data.DataManager;
import com.example.hristiyan.menu.data.Menu;
import com.example.hristiyan.menu.database.MenuDatabase;

/**
 * Created by hristiyan on 08.02.18.
 */

public class MenuApplication extends Application {
    static MenuApplication menuApplication = new MenuApplication();
    private static DataManager dataManager;

    public static MenuApplication getMenuApplication() {
        return menuApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MenuDatabase database = Room.databaseBuilder(getApplicationContext(), MenuDatabase.class, "MenuDatabase")
                .allowMainThreadQueries()
//                .addMigrations(new Migration_1_2(a1, a2a))
                .fallbackToDestructiveMigration()
                .build();
        dataManager = new DataManager(database);
    }


    public DataManager getDataManager(){
        return dataManager;
    }
}
