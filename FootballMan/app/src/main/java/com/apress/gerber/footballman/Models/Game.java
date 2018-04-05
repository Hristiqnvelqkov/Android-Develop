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
    boolean isFinished = false;
    public static final int FIRST_HALF = 1;
    public static final int SECOND_HALF = 2;
    public static final int PAUSED =3;
    public static final int RESUMED =4;
    public static final int FINISHED =5;
    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }
    private int state = PAUSED;
    private String id = UUID.randomUUID().toString();
    private int hostResult = 0;
    private int guestResult = 0;
    private Team host, guest;
    private int halfTime = 0;
    private String venue;
    private String startTime;
    private int pastTime = 0;
    private String matchDate;
    private List<Player> tempPlayers = new LinkedList<>();
    public boolean hostReady, guestReady;
    private List<GameStat> gameStat = new LinkedList<>();
    List<Player> hostPlayersInGame = new LinkedList<>();
    List<Player> guestPlayersInGame = new LinkedList<>();
    List<Player> hostTeamPlayers = new LinkedList<>();
    List<Player> guestTeamPlayers = new LinkedList<>();
    private List<Event> events = new LinkedList<>();

    public Game() {
    }
    public int getHalfTime() {
        return halfTime;
    }

    public void setHalfTime(int halfTime) {
        this.halfTime = halfTime;
    }
    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String time) {
        this.startTime = time;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public int getPastTime() {
        return pastTime;
    }

    public void setPastTime(int pastTime) {
        this.pastTime = pastTime;
    }

    public String getVenue() {
        return venue;
    }

    public void setMatchDate(String date) {
        matchDate = date;
    }

    public String getMatchDate() {
        return matchDate;
    }

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

    public String outCome(Team team) {
        String outcome = "";
        if (hostResult == guestResult) {
            return "Draw";
        }
        if (team.equals(host)) {
            if (hostResult > guestResult) {
                outcome = "Win";
            } else if (hostResult < guestResult) {
                outcome = "Lost";
            }
        } else if (team.equals(guest)) {
            if (hostResult > guestResult) {
                outcome = "Lost";
            } else if (hostResult < guestResult) {
                outcome = "Win";
            }
        }
        return outcome;
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
        if (team != null) {
            DataManager.getDataInstance().getPlayersForTeam(team, this);
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
    public Game popLastEvent(){
        Event event = events.get(events.size()-1);
        if(event.getType() == Constants.GOAL_EVENT){
            if(hostTeamPlayers.contains(event.getPlayer())){
                hostResult--;
            }else{
                guestResult--;
            }
        }else if(event.getType() == Constants.AUTO_GOAL){
            if(hostTeamPlayers.contains(event.getPlayer())){
                guestResult--;
            }else{
                hostResult--;
            }
        }
        GameStat status = findByKeyAndType(event.getPlayer(),event.getType());
        status.undoValue();
        events.remove(events.get(events.size()-1));
        return this;
    }
    public List<Event> getEvents() {
        return events;
    }

    private int getGoalsForHalf(Player player, int half) {
        int sum = 0;
        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).getPlayer().equals(player)) {
                if (events.get(i).getType() == Constants.GOAL_EVENT) {
                    if (events.get(i).getHalf() == half) {
                        sum++;
                    }
                }
            }
        }
        return sum;
    }

    public int getGoalsFirstHalfForPlayer(Player player) {
        return getGoalsForHalf(player, 1);
    }

    public int getGoalsSecondHalfForPlayer(Player player) {
        return getGoalsForHalf(player, 2);
    }

    public int getTeamSecondHalfGoals(List<Player> players) {
        int teamGoals = 0;
        for (int i = 0; i < players.size(); i++) {
            teamGoals += getGoalsSecondHalfForPlayer(players.get(i));
        }
        return teamGoals;
    }

    public int getTeamFirstHalfGoals(List<Player> players) {
        int teamGoals = 0;
        for (int i = 0; i < players.size(); i++) {
            teamGoals += getGoalsFirstHalfForPlayer(players.get(i));
        }
        return teamGoals;
    }

    public GameStat findByKeyAndType(Player player, int type) {
        for (int i = 0; i < gameStat.size(); i++) {
            Player player1 = gameStat.get(i).getPlayer();
            if ((player1.equals(player)) && gameStat.get(i).getType() == type) {
                return gameStat.get(i);
            }
        }
        return null;
    }

    public void addGoal(Player player, int time, int half) {
        if (findByKeyAndType(player, Constants.GOAL_EVENT) != null) {
            findByKeyAndType(player, Constants.GOAL_EVENT).updateValue();
            Event event = new Event(time, Constants.GOAL_EVENT, player, half);
            event.setHost(checkPlayerHost(player));
            events.add(event);
        }
    }

    public void addPenalty(Player player, int time, int half) {
        if (findByKeyAndType(player, Constants.PENALTY) != null) {
            findByKeyAndType(player, Constants.PENALTY).updateValue();
            Event event = new Event(time, Constants.PENALTY, player, half);
            event.setHost(checkPlayerHost(player));
            events.add(event);
        }
    }

    public void addAutoGoal(Player player, int time, int half) {
        if (findByKeyAndType(player, Constants.AUTO_GOAL) != null) {
            findByKeyAndType(player, Constants.AUTO_GOAL).updateValue();
            Event event = new Event(time, Constants.AUTO_GOAL, player, half);
            event.setHost(checkPlayerHost(player));
            events.add(event);
        }
    }

    public int getAutoGoals(Player player) {
        if (findByKeyAndType(player, Constants.AUTO_GOAL) != null) {
            return findByKeyAndType(player, Constants.AUTO_GOAL).getValue();
        } else {
            return 0;
        }
    }

    public void addFaul(Player player, int time, int half) {
        if (findByKeyAndType(player, Constants.FOUL_EVENT) != null) {
            findByKeyAndType(player, Constants.FOUL_EVENT).updateValue();
            Event event = new Event(time, Constants.FOUL_EVENT, player, half);
            event.setHost(checkPlayerHost(player));
            events.add(event);
        }
    }

    public void addAssist(Player player, int time, int half) {
        if (findByKeyAndType(player, Constants.ASSIST) != null) {
            findByKeyAndType(player, Constants.ASSIST).updateValue();
            Event event = new Event(time, Constants.ASSIST, player, half);
            event.setHost(checkPlayerHost(player));
            events.add(event);
        }
    }

    public void addYellowCard(Player player, int time, int half) {
        if (findByKeyAndType(player, Constants.RED_EVENT).getValue() == 0) {
            if (findByKeyAndType(player, Constants.YELLOW_EVENT).getValue() > 0) {
                addRedCard(player, time, half);
            } else {
                if (findByKeyAndType(player, Constants.YELLOW_EVENT) != null) {
                    findByKeyAndType(player, Constants.YELLOW_EVENT).updateValue();
                    Event event = new Event(time, Constants.YELLOW_EVENT, player, half);
                    event.setHost(checkPlayerHost(player));
                    events.add(event);
                }
            }
        }
    }

    public void addRedCard(Player player, int time, int half) {
        if (findByKeyAndType(player, Constants.RED_EVENT).getValue() == 0) {
            if (findByKeyAndType(player, Constants.RED_EVENT) != null) {
                findByKeyAndType(player, Constants.RED_EVENT).updateValue();
                Event event = new Event(time, Constants.RED_EVENT, player, half);
                event.setHost(checkPlayerHost(player));
                events.add(event);
            }
        }
    }

    public int getFauls(Player player) {
        if (findByKeyAndType(player, Constants.FOUL_EVENT) != null) {
            return findByKeyAndType(player, Constants.FOUL_EVENT).getValue();
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


    public int getYellowCards(Player player) {
        if (findByKeyAndType(player, Constants.YELLOW_EVENT) != null) {
            return findByKeyAndType(player, Constants.YELLOW_EVENT).getValue();
        } else {
            return 0;
        }
    }

    public int getRedCards(Player player) {
        if (findByKeyAndType(player, Constants.RED_EVENT) != null) {
            return findByKeyAndType(player, Constants.RED_EVENT).getValue();
        } else {
            return 0;
        }
    }

    public int getPenalties(Player player) {
        if (findByKeyAndType(player, Constants.PENALTY) != null) {
            return findByKeyAndType(player, Constants.PENALTY).getValue();
        } else {
            return 0;
        }
    }

    public int getGoals(Player player) {
        if (findByKeyAndType(player, Constants.GOAL_EVENT) != null) {
            return findByKeyAndType(player, Constants.GOAL_EVENT).getValue();
        } else {
            return 0;
        }
    }

    public List<GameStat> getGameStat() {
        return gameStat;
    }

    @Override
    public void onPlayersLoaded(List<Player> players) {
        tempPlayers = players;
        for (int i = 0; i < tempPlayers.size(); i++) {
            gameStat.add(new GameStat(tempPlayers.get(i), 0, Constants.GOAL_EVENT));
            gameStat.add(new GameStat(tempPlayers.get(i), 0, Constants.GOAL_EVENT));
            gameStat.add(new GameStat(tempPlayers.get(i), 0, Constants.FOUL_EVENT));
            gameStat.add(new GameStat(tempPlayers.get(i), 0, Constants.AUTO_GOAL));
            gameStat.add(new GameStat(tempPlayers.get(i), 0, Constants.PENALTY));
            gameStat.add(new GameStat(tempPlayers.get(i), 0, Constants.YELLOW_EVENT));
            gameStat.add(new GameStat(tempPlayers.get(i), 0, Constants.RED_EVENT));
            gameStat.add(new GameStat(tempPlayers.get(i), 0, Constants.ASSIST));
        }
    }
    public void setState(int state){
        this.state = state;
    }
    public int getState() {
        return state;
    }
}
