package com.apress.gerber.footballman.Models;


import java.io.Serializable;
import java.util.UUID;


public class League implements Serializable {

    private String id = UUID.randomUUID().toString();
    private String name;
   // private List<Team> teams = new LinkedList<>();

    public League() {}

    //public void setTeams(List<Team> teams) {
     //   this.teams = teams;
    //}

    //public List<Team> getTeams() {
     //   return teams;
    //}

   // public void addTeam(Team team) {
   //     teams.add(team);
  //  }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setId(String id) {
        this.id = id;
    }


}
