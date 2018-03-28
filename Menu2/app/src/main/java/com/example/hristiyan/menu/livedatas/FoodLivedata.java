package com.example.hristiyan.menu.livedatas;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Delete;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v4.util.Pair;

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
        task.execute(new Pair<String, Long>(food.getMenuId(), food.getId()));
    }

    void loadFoods(String id) {
        LoadFoodsTask task = new LoadFoodsTask();
        task.execute(id);
    }

    void updateFoodWithoutNotify(Food food) {
        UpdateFoodWithoutNotify updateTask = new UpdateFoodWithoutNotify();
        updateTask.execute(food);
    }

    void addOrUpdateFood(Food food) {
        AddOrUpdateFoodTask updateFoodTask = new AddOrUpdateFoodTask();
        updateFoodTask.execute(food);
    }

    void addFoodWitoutNotifyUi(Food food) {
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

    private class DeleteFoodTask extends AsyncTask<Pair<String, Long>, Void, List<Food>> {

        @Override
        protected synchronized List<Food> doInBackground(Pair<String, Long>... arguments) {
            Pair<String, Long> menuPair = arguments[0];
            List<Food> foods = MenuApplication.getMenuApplication().getDataManager().getFoodsForMenu(menuPair.first);
            Food deletedFood = null;
            for (Food food : foods) {
                if (food.getId() == menuPair.second) {
                    deletedFood = food;
                    MenuApplication.getMenuApplication().getDataManager().deleteFood(food);
                }
            }
            if (deletedFood != null) {
                foods.remove(deletedFood);
            }
            return foods;

        }

        @Override
        protected void onPostExecute(List<Food> foods) {
            super.onPostExecute(foods);
            postValue(foods);
        }
    }

    private class UpdateFoodWithoutNotify extends AsyncTask<Food, Void, Void> {

        @Override
        protected Void doInBackground(Food... foods) {
            Food newFood = foods[0];
            MenuApplication.getMenuApplication().getDataManager().addFoodForMenu(newFood);
            return null;
        }
    }

    private class AddOrUpdateFoodTask extends AsyncTask<Food, Void, List<Food>> {

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
