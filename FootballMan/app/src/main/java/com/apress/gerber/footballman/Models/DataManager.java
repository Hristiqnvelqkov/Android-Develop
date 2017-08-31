package com.apress.gerber.footballman.Models;

import java.util.LinkedList;
import java.util.List;


public class DataManager {
    private List<League> leagues = new LinkedList<>();
    private static DataManager manager = new DataManager();

    private DataManager() {
    }

    public static DataManager getDataInstance() {
        return manager;
    }

    public void addLeague(League league) {
        leagues.add(league);
    }

    public void removeLeague(League league) {
        leagues.remove(league);
    }

    public List<League> getLeagues() {
        return leagues;
    }

    public void addTeam(League league, Team team) {
        league.addTeam(team);
    }

    public void removeTeam(League league, Team team) {
        league.removeTeam(team);
    }

    public void addPlayer(Team team, Player player) {
        team.addPlayer(player);
    }

    public void removePlayer(Team team, Player player) {
        team.removePlayer(player);
    }


}
