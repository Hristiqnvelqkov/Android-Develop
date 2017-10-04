package com.apress.gerber.footballman.Models;


import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.internal.Keep;

@Keep
public class League extends RealmObject {
    @PrimaryKey
    private String id = UUID.randomUUID().toString();
    private String name;
    public League(){}

    private RealmList<Team> mTeamList = new RealmList<>();

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }
    public RealmList<Team> getTeamList(){
        return mTeamList;
    }
    public void setId(String id){
        this.id = id;
    }
    public void setLeagueList(RealmList<Team> teams){
        mTeamList = teams;
    }
    public RealmList<Team> getLeaguesList(){
        return mTeamList;
    }
}
