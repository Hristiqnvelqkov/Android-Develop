package com.apress.gerber.footballman.Models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;


@Entity
public class League implements Serializable{

    @PrimaryKey private int id;
    private String name;
    private List<Team> mTeamList=new LinkedList<>();

    public void setName(String name){
        this.name=name;
    }
    public int getId(){
        return this.id;
    }
    public List<Team> getTeamList(){
        return this.mTeamList;
    }
    public String getName(){
        return this.name;
    }
    public void addTeam(Team team){
        mTeamList.add(team);
    }
    public void removeTeam(Team team){
        mTeamList.remove(team);
    }
}
