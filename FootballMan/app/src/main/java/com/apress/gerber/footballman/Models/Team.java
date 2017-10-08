package com.apress.gerber.footballman.Models;


import java.io.Serializable;
import java.util.UUID;


/**
 * Created by hriso on 8/23/2017.
 */

public class Team  implements Serializable {

    private String id = UUID.randomUUID().toString();
    private League mLeague;
    private String name;

    public Team() {

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
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public League getLeague() {
        return mLeague;
    }


//    public List<Player> getPlayers() {
//        return players;
//    }
}
