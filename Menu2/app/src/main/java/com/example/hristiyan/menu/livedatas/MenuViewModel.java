package com.example.hristiyan.menu.livedatas;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.example.hristiyan.menu.data.Menu;
import com.example.hristiyan.menu.livedatas.MenuLivedata;

/**
 * Created by hristiyan on 08.02.18.
 */

public class MenuViewModel extends AndroidViewModel {
    private MenuLivedata menuLivedata;

    public MenuViewModel(@NonNull Application application) {
        super(application);
        menuLivedata = new MenuLivedata();
    }

    public void loadMenus() {
        menuLivedata.loadMenus();
    }

    public void deleteMenu(Menu menu){
        menuLivedata.deleteMenu(menu);
    }

    public void observe(LifecycleOwner activity, Observer menuObserver) {
        menuLivedata.observe(activity,menuObserver);
    }
}
