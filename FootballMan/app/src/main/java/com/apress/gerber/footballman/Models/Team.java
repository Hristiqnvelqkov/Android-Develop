package com.apress.gerber.footballman.Models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by hriso on 8/23/2017.
 */

public class Team  implements Serializable{
    private int id;
    League mLeague;
    @ColumnInfo(name = "team_name")
    private String teamName;
    private List<Player> players;
    private List<Game> mGames;

    public Team() {
        mGames = new LinkedList<>();
        players = new LinkedList<>();
    }

    public void addGame(Game game) {
        mGames.add(game);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Team team = (Team) o;

        return id == team.id;
    }
    public void setLeague(League league){
        mLeague = league;
    }
    public void setTeamName(String name) {
        teamName = name;
    }
    public String getName(){
        return teamName;
    }
    @Override
    public int hashCode() {
        return id;
    }

    public List<Game> getGames() {
        return mGames;
    }


    public List<Game> getWins() {
        List<Game> wins = new LinkedList<>();
        for (int i = 0; i < mGames.size(); i++) {
            if (mGames.get(i).winner().equals(this)) {
                wins.add(mGames.get(i));
            }
        }
        return wins;
    }
    public League getLeague(){
        return mLeague;
    }
    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public List<Player> getPlayers() {
        return players;
    }
}
