package com.apress.gerber.footballman.Models;


import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by hriso on 8/23/2017.
 */
public class Player  extends RealmObject {
    @PrimaryKey
    private String id = UUID.randomUUID().toString();
    private String name;
    private int number;
    Team team;
    private RealmList<Game> games;

    public String getId() {
        return this.id;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
    public void setTeam(Team team){
        this.team = team;
    }
    public int getGoals() {
        int goals = 0;
        for (int i = 0; i < games.size(); i++) {
            goals += games.get(i).getGoals(this);
        }
        return goals;
    }

    public int getFauls() {
        int fauls = 0;
        for (int i = 0; i < games.size(); i++) {
            fauls += games.get(i).getFauls(this);
        }
        return fauls;
    }

    public int getYellowCards() {
        int yellowCard = 0;
        for (int i = 0; i < games.size(); i++) {
            yellowCard += games.get(i).getYellowCards(this);
        }
        return yellowCard;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int num) {
        number = num;
    }

    public int getRedCards() {
        int redCard = 0;
        for (int i = 0; i < games.size(); i++) {
            redCard += games.get(i).getRedCards(this);
        }
        return redCard;
    }

    public void addGame(Game game) {
        games.add(game);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        if (number != player.number) return false;
        return name.equals(player.name);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + number;
        return result;
    }
}
