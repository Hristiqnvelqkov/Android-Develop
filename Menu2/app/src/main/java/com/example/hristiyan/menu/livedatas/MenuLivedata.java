package com.example.hristiyan.menu.livedatas;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.hristiyan.menu.MenuApplication;
import com.example.hristiyan.menu.data.DataManager;
import com.example.hristiyan.menu.data.Food;
import com.example.hristiyan.menu.data.Menu;

import java.util.List;

/**
 * Created by hristiyan on 08.02.18.
 */

public class MenuLivedata extends LiveData<List<Menu>> {

    public void loadMenus(){
        LoadMenusTask task = new LoadMenusTask();
        task.execute();
    }
    public void deleteMenu(Menu menu){
        DeleteMenusTask deleteMenusTask = new DeleteMenusTask();
        deleteMenusTask.execute(menu);
    }
    private class LoadMenusTask extends AsyncTask<Void,Void,List<Menu>>{

        @Override
        protected List<Menu> doInBackground(Void... voids) {
           return MenuApplication.getMenuApplication().getDataManager().getMenus();
        }

        @Override
        protected void onPostExecute(List<Menu> menus) {
            super.onPostExecute(menus);
            postValue(menus);

        }
    }

    private class DeleteMenusTask extends AsyncTask<Menu,Void,List<Menu>>{
        @Override
        protected List<Menu> doInBackground(Menu... menus) {
            Menu menu = menus[0];
            List<Menu> myMenus = MenuApplication.getMenuApplication().getDataManager().getMenus();
            for (Menu currentMenu : myMenus) {
                if (currentMenu.getId().equals(menu.getId())) {
                    myMenus.remove(currentMenu);
                    MenuApplication.getMenuApplication().getDataManager().deleteMenu(menu);
                    break;
                }
            }
            return myMenus;
        }

        @Override
        protected void onPostExecute(List<Menu> menus) {
            super.onPostExecute(menus);
            postValue(menus);
        }
    }
}
