package com.apress.gerber.footballman.Models;

import java.util.LinkedList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;


public class DataManager {
    Realm mRealm = Realm.getDefaultInstance();
    private List<League> leagues = new LinkedList<>();
    private static DataManager manager = new DataManager();

    private DataManager() {
    }

    public static DataManager getDataInstance() {
        return manager;
    }
    public List<League> initList(){
        return this.leagues = mRealm.where(League.class).findAll();
    }
    public void addLeague(final String string) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                League league = new League();
                league.setName(string);
                mRealm.copyToRealmOrUpdate(league);
            }
        });

    }
    public RealmResults<Team> getTeamsForLeague(League league){
        return mRealm.where(Team.class).equalTo("mLeague.name",league.getName()).findAll();

    }
    public void removeLeague(final League league) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                league.deleteFromRealm();
            }
        });
        //leagues.remove(league);
    }
    public void updateLeague(final League league, final String newName){
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                league.setName(newName);
            }
        });
    }
    public List<League> getLeagues() {
        return leagues ;
    }

    public void addTeam(final League league,final String name) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if((league!=null) ){
                    Team team = new Team();
                    team.setTeamName(name);
                    team.setLeague(league);
                    mRealm.copyToRealmOrUpdate(team);
                }
            }
        });
    }
    public void updateTeam(final Team team,final String name){
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Team update = team;
                team.setTeamName(name);
            }
        });
    }
    public void removeTeam( final Team team) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                team.deleteFromRealm();
            }
        });
    }
    public RealmResults<Player> getPlayersForTeam(Team team){
        return mRealm.where(Player.class).equalTo("team.teamName",team.getName()).findAll();

    }
    public void addPlayer(final Team team, final String name, final int number) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Player player = new Player();
                player.setTeam(team);
                player.setName(name);
                player.setNumber(number);
                mRealm.copyToRealmOrUpdate(player);
            }
        });
    }
    public void updatePlayer(final Player player, final String name, final int number){
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                player.setName(name);
                player.setNumber(number);
            }
        });
    }
    public void removePlayer(final Player player) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                player.deleteFromRealm();
            }
        });
    }

    public void addGame(final Game game){
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(game);
            }
        });
    }
    public RealmResults<Game> getGames(){
        return mRealm.where(Game.class).findAll();
    }
    public League getLeagueById(String id){
        return mRealm.where(League.class).equalTo("id",id).findFirst();
    }
    public Team getTeamById(String id){
        return mRealm.where(Team.class).equalTo("id",id).findFirst();
    }
    public Game getGameById(String id){
        return mRealm.where(Game.class).equalTo("id",id).findFirst();
    }
    public Player getPlayerById(String id){
        return mRealm.where(Player.class).equalTo("id",id).findFirst();
    }

}
