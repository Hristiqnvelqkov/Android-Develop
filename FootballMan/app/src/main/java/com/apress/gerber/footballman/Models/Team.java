package com.apress.gerber.footballman.Models;



import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by hriso on 8/23/2017.
 */

public class Team extends RealmObject  {
    @PrimaryKey
    private String id = UUID.randomUUID().toString();
    League mLeague;
    private String teamName;
    private RealmList<Player> players;
    private RealmList<Game> mGames;

    public Team() {
        mGames = new RealmList<>();
        players = new RealmList<>();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Team team = (Team) o;

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
