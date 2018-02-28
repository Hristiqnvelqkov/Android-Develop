package com.example.hristiyan.menu;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.ContactsContract;
import android.util.Log;

import com.example.hristiyan.menu.data.DataManager;
import com.example.hristiyan.menu.data.Menu;
import com.example.hristiyan.menu.database.MenuDatabase;

/**
 * Created by hristiyan on 08.02.18.
 */

public class MenuApplication extends Application {
    static MenuApplication menuApplication = new MenuApplication();
    private static DataManager dataManager;
    private static SharedPreferences preferences;
    private boolean isDemoVersion = true;

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
        preferences = getSharedPreferences(getString(R.string.preference_file), MODE_PRIVATE);
        isDemoVersion = preferences.getBoolean(getString(R.string.preference_file), true);
        dataManager = new DataManager(database);
        final String buildSerial;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            buildSerial = Build.getSerial();
        } else {
            buildSerial = Build.SERIAL;
        }
        Log.d("HRISKO", buildSerial);

    }

    public static SharedPreferences getPreferences() {
        return preferences;
    }

    public boolean getDemoVersion() {
        return isDemoVersion;
    }

    public DataManager getDataManager() {
        return dataManager;
    }
}
