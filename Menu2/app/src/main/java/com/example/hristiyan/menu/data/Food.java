package com.example.hristiyan.menu.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by hristiyan on 07.02.18.
 */
@Entity
public class Food implements Serializable {
    @PrimaryKey
    @NonNull
    private String id;
    private String menuId;
    private String name;
    private int timesSelected = 0;

    public Food(String menuId) {
        id = UUID.randomUUID().toString();
        this.menuId = menuId;
    }

    public String getMenuId() {
        return menuId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Food food = (Food) o;

        return id.equals(food.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    void selectFood() {
        timesSelected++;
    }

    public void setTimesSelected(int timesSelected) {
        this.timesSelected = timesSelected;
    }

    public int getTimesSelected() {
        return timesSelected;
    }

}
