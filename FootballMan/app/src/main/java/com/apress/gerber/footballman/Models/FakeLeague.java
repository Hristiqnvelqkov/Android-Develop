package com.apress.gerber.footballman.Models;


import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import io.realm.annotations.PrimaryKey;

public class FakeLeague implements Serializable {
    @PrimaryKey
    private String id;
    private String name;
    public List<FakeTeam> teams = new LinkedList<>();
    public FakeLeague(League league) {
        id = league.getId();
        name = league.getName();


    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    //    public RealmList<Team> getTeamList(){
//        return mTeamList;
//    }
    public void setId(String id) {
        this.id = id;
    }
}
//    public void setLeagueList(RealmList<Team> teams){
//        mTeamList = teams;
//    }
//    public RealmList<Team> getLeaguesList(){
//        return mTeamList;
//    }
//}
