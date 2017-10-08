package com.apress.gerber.footballman.Models;


import com.apress.gerber.footballman.Constants;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;


/**
 * Created by hriso on 8/23/2017.
 */

public class Game implements Serializable, DataManager.OnPlayersLoaded {

    private String id = UUID.randomUUID().toString();
    private int hostResult = 0;
    private int guestResult = 0;
    private Team host, guest;
    private List<Player> tempPlayers = new LinkedList<>();
    public boolean hostReady, guestReady;
    private List<GameStat> gameStat = new LinkedList<>();
    List<Player> hostPlayersInGame = new LinkedList<>();
    List<Player> guestPlayersInGame = new LinkedList<>();
    List<Player> hostTeamPlayers = new LinkedList<>();
    List<Player> guestTeamPlayers = new LinkedList<>();
    private List<Event> events = new LinkedList<>();
    public Game(){}
    public void enterInGame(Player player) {

        if (hostTeamPlayers.contains(player)) {
            hostPlayersInGame.add(player);
        } else {
            guestPlayersInGame.add(player);
        }
    }

    public void outOfGame(Player player) {
        if (hostTeamPlayers.contains(player)) {
            hostPlayersInGame.remove(player);
        } else {
            guestPlayersInGame.remove(player);
        }
    }

    public List<Player> getHostPlayersInGame() {
        return hostPlayersInGame;
    }

    public List<Player> getGuestPlayersInGame() {
        return guestPlayersInGame;
    }


    public void setHost(Team team) {
        host = team;
        setDefaultValues(host);
    }

    public void setGuest(Team team) {
        guest = team;
        setDefaultValues(guest);
    }

    public int getHostResult() {
        return hostResult;
    }

    public int getGuestResult() {
        return guestResult;
    }

    public int updateResult(Team team) {
        if (team == host) {
            return ++hostResult;
        } else {
            return ++guestResult;
        }
    }

    public int getTeamRedCards(List<Player> players) {
        int redCards = 0;
        for (int i = 0; i < players.size(); i++) {
            redCards += getRedCards(players.get(i));
        }
        return redCards;
    }

    public int getTeamYellowCards(List<Player> players) {
        int yellowCards = 0;
        for (int i = 0; i < players.size(); i++) {
            yellowCards += getYellowCards(players.get(i));
        }
        return yellowCards;
    }

    public int getTeamFauls(List<Player> players) {
        int fauls = 0;
        for (int i = 0; i < players.size(); i++) {
            fauls += getFauls(players.get(i));
        }
        return fauls;
    }

    public Team getHost() {
        return host;
    }

    public Team getGuest() {
        return guest;
    }

    private void setDefaultValues(Team team) {
        if(team != null) {
            DataManager.getDataInstance().getPlayersForTeam(team,this);
        }
    }

    public void removeAllGuestPlayers() {
        guestTeamPlayers.clear();
    }

    public void removeAllHostPlayers() {
        hostTeamPlayers.clear();
    }

    public boolean readyHost() {
        return hostReady;
    }

    public boolean readyGuest() {
        return guestReady;
    }

    public void setHostReady() {
        hostReady = true;
    }

    public void setGuestReady() {
        guestReady = true;
    }

    public void addHostPlayer(Player player) {
        hostTeamPlayers.add(player);
    }

    public List<Player> getHostPlayers() {
        return hostTeamPlayers;
    }

    public List<Player> getGuestTeamPlayers() {
        return guestTeamPlayers;
    }

    public void addGuestPlayer(Player player) {
        guestTeamPlayers.add(player);
    }

    public void removeHostPlayer(Player player) {
        hostTeamPlayers.remove(player);
    }

    public void removeGuestPlayer(Player player) {
        guestTeamPlayers.remove(player);
    }

    public String getId() {
        return this.id;
    }


    public boolean checkPlayerHost(Player player) {
        if (hostTeamPlayers.contains(player)) {
            return true;
        } else {
            return false;
        }
    }

    public List<Event> getEvents() {
        return events;
    }

    public GameStat findByKeyAndType(Player player, int type) {
        for (int i = 0; i < gameStat.size(); i++) {
            if ((gameStat.get(i).getPlayer().equals(player)) && gameStat.get(i).getType() == type) {
                return gameStat.get(i);
            }
        }
        return null;
    }

    public void addGoal(Player player, int time) {
        if (findByKeyAndType(player, Constants.GOAL) != null) {
            findByKeyAndType(player, Constants.GOAL).updateValue();
            Event event = new Event(time, Constants.GOAL_EVENT, player);
            event.setHost(checkPlayerHost(player));
            events.add(event);
        }
    }

    public void addFaul(Player player, int time) {
        if (findByKeyAndType(player, Constants.FOUL) != null) {
            findByKeyAndType(player, Constants.FOUL).updateValue();
            Event event = new Event(time, Constants.FOUL_EVENT, player);
            event.setHost(checkPlayerHost(player));
            events.add(event);
        }
    }

    public void addAssist(Player player) {
        if (findByKeyAndType(player, Constants.ASSIST) != null) {
            findByKeyAndType(player, Constants.ASSIST).updateValue();
        }
    }

    public void addYellowCard(Player player, int time) {
        if (findByKeyAndType(player, Constants.RED_CARD).getValue() == 0) {
            if (findByKeyAndType(player, Constants.YELLOW_CARD).getValue() > 0) {
                addRedCard(player, time);
            } else {
                if (findByKeyAndType(player, Constants.YELLOW_CARD) != null) {
                    findByKeyAndType(player, Constants.YELLOW_CARD).updateValue();
                    Event event = new Event(time, Constants.YELLOW_EVENT, player);
                    event.setHost(checkPlayerHost(player));
                    events.add(event);
                }
            }
        }
    }

    public void addRedCard(Player player, int time) {
        if (findByKeyAndType(player, Constants.RED_CARD).getValue() == 0) {
            if (findByKeyAndType(player, Constants.RED_CARD) != null) {
                findByKeyAndType(player, Constants.RED_CARD).updateValue();
                Event event = new Event(time, Constants.RED_EVENT, player);
                event.setHost(checkPlayerHost(player));
                events.add(event);
            }
        }
    }

    public int getFauls(Player player) {
        if (findByKeyAndType(player, Constants.FOUL) != null) {
            return findByKeyAndType(player, Constants.FOUL).getValue();
        } else {
            return 0;
        }
    }

    public int getAssist(Player player) {
        if (findByKeyAndType(player, Constants.ASSIST) != null) {
            return findByKeyAndType(player, Constants.ASSIST).getValue();
        } else {
            return 0;
        }
    }

    public boolean checkPlayerInGame(Player player) {
        if (getGuestPlayersInGame().contains(player) || getHostPlayersInGame().contains(player)) {
            return true;
        } else {
            return false;
        }
    }

//    @Nullable
//    Team winner() {
//        Team winner;
//        int hostGoals = 0;
//        int guestGoals = 0;
//        for (int i = 0; i < host.getPlayers().size(); i++) {
//            hostGoals += host.getPlayers().get(i).getGoals();
//            guestGoals += guest.getPlayers().get(i).getGoals();
//        }
//        if (hostGoals > guestGoals) {
//            winner = host;
//        } else {
//            winner = guest;
//        }
//        return winner;
//    }

    public int getYellowCards(Player player) {
        if (findByKeyAndType(player, Constants.YELLOW_CARD) != null) {
            return findByKeyAndType(player, Constants.YELLOW_CARD).getValue();
        } else {
            return 0;
        }
    }

    public int getRedCards(Player player) {
        if (findByKeyAndType(player, Constants.RED_CARD) != null) {
            return findByKeyAndType(player, Constants.RED_CARD).getValue();
        } else {
            return 0;
        }
    }

    public int getGoals(Player player) {
        if (findByKeyAndType(player, Constants.GOAL) != null) {
            return findByKeyAndType(player, Constants.GOAL).getValue();
        } else {
            return 0;
        }
    }


    @Override
    public void onPlayersLoaded(List<Player> players) {
        tempPlayers = players;
        for (int i = 0; i < tempPlayers.size(); i++) {
            gameStat.add(new GameStat(tempPlayers.get(i), 0, Constants.GOAL));
            gameStat.add(new GameStat(tempPlayers.get(i), 0, Constants.GOAL));
            gameStat.add(new GameStat(tempPlayers.get(i), 0, Constants.FOUL));
            gameStat.add(new GameStat(tempPlayers.get(i), 0, Constants.YELLOW_CARD));
            gameStat.add(new GameStat(tempPlayers.get(i), 0, Constants.RED_CARD));
            gameStat.add(new GameStat(tempPlayers.get(i), 0, Constants.ASSIST));
        }
    }
}
