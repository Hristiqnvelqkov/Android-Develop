package com.apress.gerber.footballman;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by hristiyan on 29.10.17.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
