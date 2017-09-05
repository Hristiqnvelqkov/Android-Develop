package com.apress.gerber.footballman.Models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import io.reactivex.annotations.Nullable;

/**
 * Created by hriso on 8/23/2017.
 */
@Entity
public class Game implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    List<Player> hostTeamPlayers = new LinkedList<>();
    List<Player> guestTeamPlayers = new LinkedList<>();
    private Team host, guest;
    private HashMap<Player, Integer> goals = new HashMap<>();
    private HashMap<Player, Integer> fauls = new HashMap<>();
    private HashMap<Player, Integer> yellowCards = new HashMap<>();
    private HashMap<Player, Integer> redCards = new HashMap<>();

    public void setHost(Team team){
        host = team;
        setDefaultValues(host);
    }
    public void setGuest(Team team){
        guest = team;
        setDefaultValues(guest);
    }
    public Team getHost(){
        return host;
    }
    public Team getGuest(){
        return guest;
    }
    private void setDefaultValues(Team team){
        for (int i = 0; i < team.getPlayers().size(); i++) {
            goals.put(team.getPlayers().get(i), 0);
            fauls.put(team.getPlayers().get(i), 0);
            yellowCards.put(team.getPlayers().get(i), 0);
            redCards.put(team.getPlayers().get(i), 0);
        }
    }
    public boolean readyHost(){
        boolean status = false;
        if(this.hostTeamPlayers.size()>1){
            status=true;
        }
        return status;
    }
    public boolean readyGuest(){
        boolean status = false;
        if(this.guestTeamPlayers.size()>1){
            status=true;
        }
        return status;
    }
    public void addHostPlayer(Player player){
        hostTeamPlayers.add(player);
    }
    public void addGuestPlayer(Player player){
        guestTeamPlayers.add(player);
    }
    public void removeHostPlayer(Player player){
        hostTeamPlayers.remove(player);
    }
    public void removeGuestPlayer(Player player){
        hostTeamPlayers.remove(player);
    }
    public int getId(){
        return this.id;
    }
    public void addGoal(Player player) {
        goals.put(player,goals.get(player)+1);
    }
    public void addFaul(Player player) {
        fauls.put(player,fauls.get(player)+1);
    }
    public void addYellowCard(Player player){
        yellowCards.put(player,yellowCards.get(player)+1);
    }

    public void addRedCard(Player player){
        redCards.put(player,yellowCards.get(player)+1);
    }

    int getFauls(Player player){
        return fauls.get(player);
    }

    @Nullable
    Team winner(){
        Team winner;
        int hostGoals=0;
        int guestGoals=0;
        for(int i=0;i<host.getPlayers().size();i++){
               hostGoals+=host.getPlayers().get(i).getGoals();
               guestGoals+=guest.getPlayers().get(i).getGoals();
        }
        if(hostGoals > guestGoals){
            winner = host;
        }else{
            winner=guest;
        }
        return winner;
    }
    int getYellowCards(Player player){
        return yellowCards.get(player);
    }

    int getRedCards(Player player){
        return redCards.get(player);
    }

    int getGoals(Player player){
        return goals.get(player);
    }
}
