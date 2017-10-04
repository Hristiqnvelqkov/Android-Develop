package com.apress.gerber.footballman.Models;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.LinkedList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;


public class DataManager {
    Realm mRealm = Realm.getDefaultInstance();
    private List<League> leagues = new LinkedList<>();
    private static DataManager manager = new DataManager();

    private DatabaseReference mReference = FirebaseDatabase.getInstance().getReference("leagues");
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
                mReference.child(league.getName()).removeValue();
                league.deleteFromRealm();
            }
        });

        //leagues.remove(league);
    }
    public void updateLeague(final League league, final String newName){
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                DatabaseReference reference = mReference.child(league.getId());
                league.setName(newName);
                reference.child("name").setValue(newName);

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
                    mReference.child(league.getId()).child("teams").child(team.getId()).setValue(new FakeTeam(team));

                }
            }
        });
    }
    public void updateTeam(final Team team,final String name){
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
               DatabaseReference reference = mReference.child(team.getLeague().getId()).child("teams").child(team.getId());
                Team update = team;
                team.setTeamName(name);
                reference.child("name").setValue(name);
             //   mReference.child(team.getLeague().getName()).child("teams").child(team.getName()).setValue(new FakeTeam(team));
            }
        });
    }
    public void removeTeam( final Team team) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                mReference.child(team.getLeague().getId()).child("teams").child(team.getId()).removeValue();
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
                mReference.child(team.getLeague().getId()).child("teams").child(team.getId()).child("players").child(player.getId()).setValue(new FakePlayer(player));
            }
        });
    }
    public void updatePlayer(final Player player, final String name, final int number){
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                DatabaseReference reference = mReference.child(player.getTeam().getLeague().getId()).child("teams").child(player.getTeam().getId()).child("players").child(player.getId());
                player.setName(name);
                player.setNumber(number);
                reference.setValue(new FakePlayer(player));

            }
        });
    }
    public void removePlayer(final Player player) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                mReference.child(player.getTeam().getLeague().getId()).child("teams").child(player.getTeam().getId()).child("players").child(player.getId()).removeValue();
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
    public League getLastLeague(){
        return leagues.get(((int) mRealm.where(League.class).count()) - 1 );
    }
}
