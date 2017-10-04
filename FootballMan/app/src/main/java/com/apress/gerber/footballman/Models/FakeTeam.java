package com.apress.gerber.footballman.Models;



import java.io.Serializable;
import java.util.List;

import io.realm.RealmList;

/**
 * Created by hriso on 8/23/2017.
 */

public class FakeTeam implements Serializable  {

    private String id ;
    League mLeague;
    private String teamName;
    public List<Player> playerList;
    private RealmList<Player> players;
    private RealmList<Game> mGames;
    public List<Game> games;

    public FakeTeam(Team team) {
        teamName = team.getName();
        id = team.getId();
        playerList = team.getPlayers();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FakeTeam team = (FakeTeam) o;

        return id == team.id;
    }

    public String getId() {
        return this.id;
    }

    public void setLeague(League league) {
        mLeague = league;
    }

    public void setTeamName(String name) {
        teamName = name;
    }

    public String getName() {
        return teamName;
    }

    public League getLeague() {
        return mLeague;
    }


    public List<Player> getPlayers() {
        return players;
    }
}
