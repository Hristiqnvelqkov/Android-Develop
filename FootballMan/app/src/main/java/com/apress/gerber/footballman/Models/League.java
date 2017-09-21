package com.apress.gerber.footballman.Models;


import android.os.Parcel;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;


import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class League extends RealmObject implements Serializable {
    private int id;
    private String name;
    private RealmList<Team> mTeamList = new RealmList<>();

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public List<Team> getTeamList() {
        return this.mTeamList;
    }

    public String getName() {
        return this.name;
    }

    public void addTeam(Team team) {
        mTeamList.add(team);
    }

    public void removeTeam(Team team) {
        mTeamList.remove(team);
    }
}
