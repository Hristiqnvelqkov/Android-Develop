package com.example.hristiyan.menu.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Created by hristiyan on 07.02.18.
 */
@Entity
public class Menu implements Serializable {
    @PrimaryKey
    @NonNull
    private String id;
    @Ignore
    private List<Food> foods = new LinkedList<>();
    private String name;
    private boolean isActive;

    public String getName() {
        return name;
    }

    public Menu() {
        id = UUID.randomUUID().toString();
    }

    public void setName(String name) {
        this.name = name;
    }

    private long date;

    public boolean isActive() {
        return isActive;
    }

    public String getId() {
        return id;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setDate(long date) {
        this.date = date;
    }
    public void setId(String id){
        this.id = id;
    }
    public long getDate() {
        return date;
    }

    public void addFood(Food food) {
        foods.add(food);
    }

    public void selectFood(Food food) {
        food.selectFood();
    }

    public List<Food> getFoods() {
        return foods;
    }
}
