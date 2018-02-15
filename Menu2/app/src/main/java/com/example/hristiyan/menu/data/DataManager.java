package com.example.hristiyan.menu.data;

import android.arch.persistence.room.Room;

import com.example.hristiyan.menu.MenuApplication;
import com.example.hristiyan.menu.database.MenuDatabase;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by hristiyan on 07.02.18.
 */

public class DataManager {
    private List<Menu> menus;
    private MenuDatabase mydatabase;

    public DataManager(MenuDatabase database) {
        this.mydatabase = database;
    }

    public void addMenu(Menu menu) {
        menus.add(menu);
        mydatabase.menuDao().insertMenu(menu);
    }

    public Menu getActiveMenu(){
        Menu activeMenu = null;
        for(Menu menu : mydatabase.menuDao().getAllMenus()){
            if(menu.isActive()){
                activeMenu = menu;
                break;
            }
        }
        return activeMenu;
    }
    public List<Menu> getMenus(){
        menus = mydatabase.menuDao().getAllMenus();
        return menus;
    }

    public void setLocalMenus(List<Menu> menus){
        this.menus = menus;
    }

    public List<Menu> getLocalMenus(){
        return menus;
    }

    public void addFoodForMenu(Food food){
        mydatabase.FoodDao().insertFood(food);
    }
    public List<Food> getFoodsForMenu(String id){
        return mydatabase.FoodDao().getFoodsForMenu(id);
    }
    public void deleteFood(Food food){
        mydatabase.FoodDao().deleteFood(food);
    }

    public Food getFoodById(String id){
       return mydatabase.FoodDao().getFoodById(id);
    }
    public void updateMenu(Menu menu) {
        mydatabase.menuDao().updateMenu(menu);
    }
    public void deleteMenu(Menu menu){
        mydatabase.menuDao().deleteMenu(menu);
    }
}
