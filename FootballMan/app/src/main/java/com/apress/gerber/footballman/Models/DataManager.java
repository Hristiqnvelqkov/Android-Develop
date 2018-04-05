package com.apress.gerber.footballman.Models;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;


public class DataManager implements Serializable {

    private static DataManager manager = new DataManager();
    private List<Game> games = new LinkedList<>();
    private DatabaseReference gameReference = FirebaseDatabase.getInstance().getReference("games");
    private DatabaseReference mReference = FirebaseDatabase.getInstance().getReference("leagues");

    private DataManager() {
    }

    public static DataManager getDataInstance() {
        return manager;
    }

    public void addLeague(final String string) {
        League league = new League();
        league.setName(string);
        mReference.child(league.getId()).setValue(league);

    }

    public void initGames(List<Game> games) {
    }

    public void getTeamsForLeague(final League league, final OnTeamsLoaded listner) {
        final List<Team> leagueTeams = new LinkedList<>();
        mReference.child(league.getId()).child("teams").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Team team = snapshot.getValue(Team.class);
                    leagueTeams.add(team);
                }
                listner.onTeamLoaded(leagueTeams);
                mReference.child(league.getId()).child("teams").removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void removeLeague(final League league) {
        mReference.child(league.getId()).removeValue();
    }

    public void updateLeague(final League league, final String newName) {
        DatabaseReference reference = mReference.child(league.getId());
        league.setName(newName);
        reference.child("name").setValue(newName);
    }

    public void getLeagues(final OnLeagesLoaded listener) {
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<League> leagues = new LinkedList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    League league = snapshot.getValue(League.class);
                    leagues.add(league);
                }
                listener.onLeaguesLoaded(leagues);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void addTeam(League league, String name) {
        if ((league != null)) {
            Team team = new Team();
            team.setTeamName(name);
            team.setLeague(league);
            // league.addTeam(team);
            // mReference.child(league.getId()).child("teams").child(team.getId()).push();
            //mReference.child(league.getId()).setValue(league);
            mReference.child(league.getId()).child("teams").child(team.getId()).setValue(team);
        }


    }

    public void updateTeam(final Team team, final String name) {
        DatabaseReference reference = mReference.child(team.getLeague().getId()).child("teams").child(team.getId());
        Team update = team;
        team.setTeamName(name);
        reference.child("name").setValue(name);

    }

    public void removeTeam(final Team team) {
        mReference.child(team.getLeague().getId()).child("teams").child(team.getId()).removeValue();
    }

    public void getPlayersForTeam(final Team team, final OnPlayersLoaded listner) {
        mReference.child(team.getLeague().getId()).child("teams").child(team.getId()).child("players").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<Player> players = new LinkedList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Player player = snapshot.getValue(Player.class);
                    players.add(player);
                }
                mReference.child(team.getLeague().getId()).child("teams").child(team.getId()).child("players").removeEventListener(this);
                listner.onPlayersLoaded(players);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void addPlayer(final Team team, final String name, final int number) {
        Player player = new Player();
        player.setTeam(team);
        player.setName(name);
        player.setNumber(number);
        mReference.child(team.getLeague().getId()).child("teams").child(team.getId()).child("players").child(player.getId()).setValue(player);

    }

    public void updatePlayer(final Player player, final String name, final int number) {
        DatabaseReference reference = mReference.child(player.getTeam().getLeague().getId()).child("teams").child(player.getTeam().getId()).child("players").child(player.getId());
        player.setName(name);
        player.setNumber(number);
        reference.setValue(player);


    }

    public void removePlayer(final Player player) {
        mReference.child(player.getTeam().getLeague().getId()).child("teams").child(player.getTeam().getId()).child("players").child(player.getId()).removeValue();

    }

    public void addGame(final Game game) {
        gameReference.child(game.getId()).setValue(game);
    }

    public List<Game> getGames(final onGamesLoaded listener) {
        final List<Game> games = new LinkedList<>();
        gameReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Game game = snapshot.getValue(Game.class);
                    games.add(game);
                }
                listener.onGamesLoaded(games);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return games;
    }

    public interface OnLeagesLoaded {
        void onLeaguesLoaded(List<League> leagues);
    }

    public interface OnTeamsLoaded {
        void onTeamLoaded(List<Team> teams);
    }

    public interface OnPlayersLoaded {
        void onPlayersLoaded(List<Player> players);
    }

    public interface onGamesLoaded {
        void onGamesLoaded(List<Game> games);
    }
}
