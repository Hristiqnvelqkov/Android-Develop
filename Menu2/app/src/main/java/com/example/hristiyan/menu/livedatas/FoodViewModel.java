package com.example.hristiyan.menu.livedatas;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;

import com.example.hristiyan.menu.data.Food;
import com.example.hristiyan.menu.livedatas.FoodLivedata;

/**
 * @author hristiyan
 *
 */

public class FoodViewModel extends AndroidViewModel {
    private FoodLivedata foodLivedata;

    public FoodViewModel(@NonNull Application application) {
        super(application);
        foodLivedata = new FoodLivedata();
    }

    public void addOrUpdateFood(Food food){
        foodLivedata.addOrUpdateFood(food);
    }
    public void addFoodWithoutNotifyUi(Food food){
        foodLivedata.addFoodWitoutNotifyUi(food);
    }
    public void loadFoodsForMenu(String menuId){
        foodLivedata.loadFoods(menuId);
    }
    public void deleteFood(Food food){
        foodLivedata.deleteFood(food);
    }
    public void updateFoodWithoutNotify(Food food){
        foodLivedata.updateFoodWithoutNotify(food);
    }

    public void observe(LifecycleOwner activity, Observer foodObserver) {
        foodLivedata.observe(activity,foodObserver);
    }
}
