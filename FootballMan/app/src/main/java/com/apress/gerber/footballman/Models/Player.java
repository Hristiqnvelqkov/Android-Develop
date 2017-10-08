package com.apress.gerber.footballman.Models;


import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * Created by hriso on 8/23/2017.
 */
public class Player implements Serializable {

    private String id = UUID.randomUUID().toString();
    private String name;
    private int number;
    Team team;
    private List<Game> games;

    public Player() {
    }

    public String getId() {
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

//    public int getGoals() {
//        int goals = 0;
//        for (int i = 0; i < games.size(); i++) {
//            goals += games.get(i).getGoals(this);
//        }
//        return goals;
//    }

//    public int getFauls() {
//        int fauls = 0;
//        for (int i = 0; i < games.size(); i++) {
//            fauls += games.get(i).getFauls(this);
//        }
//        return fauls;
//    }

    public Team getTeam() {
        return team;
    }

//    public int getYellowCards() {
//        int yellowCard = 0;
//        for (int i = 0; i < games.size(); i++) {
//            yellowCard += games.get(i).getYellowCards(this);
//        }
//        return yellowCard;
//    }

    public int getNumber() {
        return this.number;
    }

    public void setNumber(int num) {
        this.number = num;
    }

//    public int getRedCards() {
//        int redCard = 0;
//        for (int i = 0; i < games.size(); i++) {
//            redCard += games.get(i).getRedCards(this);
//        }
//        return redCard;
//    }

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
