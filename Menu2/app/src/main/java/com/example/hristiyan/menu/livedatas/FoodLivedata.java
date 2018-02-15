package com.example.hristiyan.menu.livedatas;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Delete;
import android.os.AsyncTask;
import android.provider.ContactsContract;

import com.example.hristiyan.menu.MenuApplication;
import com.example.hristiyan.menu.data.DataManager;
import com.example.hristiyan.menu.data.Food;
import com.example.hristiyan.menu.data.Menu;

import java.util.List;

/**
 * Created by hristiyan on 08.02.18.
 */

public class FoodLivedata extends LiveData<List<Food>> {

    void deleteFood(Food food) {
        DeleteFoodTask task = new DeleteFoodTask();
        task.execute(food.getMenuId(), food.getId());
    }

    void loadFoods(String id) {
        LoadFoodsTask task = new LoadFoodsTask();
        task.execute(id);
    }

    void updateFoodWithoutNotify(Food food){
        UpdateFoodWithoutNotify updateTask = new UpdateFoodWithoutNotify();
        updateTask.execute(food);
    }

    void addOrUpdateFood(Food food) {
        AddOrUpdateFoodTask updateFoodTask = new AddOrUpdateFoodTask();
        updateFoodTask.execute(food);
    }
    void addFoodWitoutNotifyUi(Food food){
        UpdateFoodWithoutNotify addFoodTask = new UpdateFoodWithoutNotify();
        addFoodTask.execute(food);
    }
    private class LoadFoodsTask extends AsyncTask<String, Void, List<Food>> {

        @Override
        protected List<Food> doInBackground(String... strings) {
            return MenuApplication.getMenuApplication().getDataManager().getFoodsForMenu(strings[0]);
        }

        @Override
        protected void onPostExecute(List<Food> foods) {
            super.onPostExecute(foods);
            postValue(foods);
        }
    }

    private class DeleteFoodTask extends AsyncTask<String, Void, List<Food>> {

        @Override
        protected synchronized List<Food> doInBackground(String... arguments) {
            String menuId = arguments[0];
            String foodId = arguments[1];
            List<Food> foods = MenuApplication.getMenuApplication().getDataManager().getFoodsForMenu(menuId);
            for (Food food : foods) {
                if (food.getId().equals(foodId)) {
                    foods.remove(food);
                    MenuApplication.getMenuApplication().getDataManager().deleteFood(food);
                }
            }
            return foods;

        }

        @Override
        protected void onPostExecute(List<Food> foods) {
            super.onPostExecute(foods);
            postValue(foods);
        }
    }
    private class UpdateFoodWithoutNotify extends AsyncTask<Food,Void,Void>{

        @Override
        protected Void doInBackground(Food... foods) {
            Food newFood = foods[0];
            MenuApplication.getMenuApplication().getDataManager().addFoodForMenu(newFood);
            return null;
        }
    }

    private class AddOrUpdateFoodTask extends AsyncTask<Food,Void,List<Food>>{

        @Override
        protected List<Food> doInBackground(Food... foods) {
            Food newFood = foods[0];
            MenuApplication.getMenuApplication().getDataManager().addFoodForMenu(newFood);
            return MenuApplication.getMenuApplication().getDataManager().getFoodsForMenu(newFood.getMenuId());
        }

        @Override
        protected void onPostExecute(List<Food> foods) {
            super.onPostExecute(foods);
            postValue(foods);
        }
    }
}
