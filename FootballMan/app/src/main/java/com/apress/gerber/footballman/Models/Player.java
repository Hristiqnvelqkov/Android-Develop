package com.apress.gerber.footballman.Models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hriso on 8/23/2017.
 */
@Entity
public class Player implements Serializable{

    @PrimaryKey
    private int id;
    private String name;
    private int number;
    private List<Game> games;
    public int getId() {
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public int getGoals() {
        int goals = 0;
        for (int i = 0; i < games.size(); i++) {
            goals+=games.get(i).getGoals(this);
        }
        return goals;
    }
    public int getFauls(){
        int fauls=0;
        for (int i = 0; i < games.size(); i++) {
            fauls+=games.get(i).getFauls(this);
        }
        return fauls;
    }
    public int getYellowCards(){
        int yellowCard=0;
        for (int i = 0; i < games.size(); i++) {
            yellowCard+=games.get(i).getYellowCards(this);
        }
        return yellowCard;
    }
    public int getNumber(){
        return number;
    }
    public void setNumber(int num){
        number = num;
    }
    public int getRedCards(){
        int redCard=0;
        for (int i = 0; i < games.size(); i++) {
            redCard+=games.get(i).getRedCards(this);
        }
        return redCard;
    }

    public void addGame(Game game) {
        games.add(game);
    }
}
