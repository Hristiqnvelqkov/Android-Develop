package com.apress.gerber.footballman;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.apress.gerber.footballman.Models.DataManager;
import com.apress.gerber.footballman.Models.Game;
import com.apress.gerber.footballman.Models.League;
import com.apress.gerber.footballman.Models.Player;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ExportData implements DataManager.OnLeagesLoaded, DataManager.onGamesLoaded {
    private static ExportData data = new ExportData();
    private File file;
    Activity activity;
    private CSVWriter csvWriter;
    private Game lastExportedGame;
    public void setLastExportedGame(Game game){
        lastExportedGame = game;
    }
    private ExportData() {
    }

    public static ExportData getInstance() {
        return data;
    }

    public void writeToFile(String fileName, Activity activity, int exportType) {
        this.activity = activity;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + fileName);
            try {
                csvWriter = new CSVWriter(new FileWriter(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
            int permission = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permission == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            } else {
                if (exportType == Constants.EXPORT_LEAGUES) {
                    DataManager.getDataInstance().getLeagues(this);
                } else if (exportType == Constants.EXPORT_GAMES) {
                    DataManager.getDataInstance().getGames(this);
                }else if(exportType == Constants.EXPORT_GAME){
                    List<Game> games = new LinkedList<>();
                    games.add(lastExportedGame);
                    onGamesLoaded(games);
                }
            }

        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    DataManager.getDataInstance().getLeagues(this);
                } else {
                }
            }

        }
    }

    @Override
    public void onLeaguesLoaded(List<League> leagues) {
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(file);
            for (League league : leagues) {
                fileOutputStream.write(league.getName().getBytes());
            }
            fileOutputStream.flush();
            fileOutputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGamesLoaded(List<Game> games) {
        List<String[]> data = new LinkedList<>();
        data.add(new String[]{"DATE","TIME","VENUE","TEAMS","RESULTS","OUTCOME", "PLAYERS", "GOALS", "ASSIST", "YELLOW CARDS", "RED CARDS","FOULS"});
        for (Game game : games){
            int counter =0;
            for(Player player : game.getHostPlayers()){
                System.out.println(game.getGoals(player));
                if (counter == 0) {
                    data.add(new String[]{"",game.getStartTime(),game.getVenue(),game.getHost().getName(),game.getTeamFirstHalfGoals(game.getHostPlayers())+"|"
                            +game.getTeamSecondHalfGoals(game.getHostPlayers())+"|"+game.getHostResult()
                            ,game.outCome(game.getHost()),game.getHostPlayers().get(counter).getName(),game.getGoals(player)+""
                            ,game.getAssist(player)+"",game.getYellowCards(player)+"",game.getRedCards(player)+"",game.getFauls(player)+""});
                }else {
                    data.add(new String[]{"","","","",""
                            , "", game.getHostPlayers().get(counter).getName(), game.getGoals(player) + ""
                            , game.getAssist(player) + "", game.getYellowCards(player) + "", game.getRedCards(player) + "",game.getFauls(player)+""});
                }
                counter++;
            }
            int counter1 =0;
            for(Player player : game.getGuestTeamPlayers()){
                System.out.println(game.getGoals(player));
                if(counter1 ==0 ) {
                    data.add(new String[]{"","","",game.getGuest().getName(), game.getTeamFirstHalfGoals(game.getGuestTeamPlayers()) + "|"
                            + game.getTeamSecondHalfGoals(game.getGuestPlayersInGame()) + "|" + game.getGuestResult()
                            , game.outCome(game.getGuest()), game.getGuestTeamPlayers().get(counter1).getName(), game.getGoals(player) + ""
                            , game.getAssist(player) + "", game.getYellowCards(player) + "", game.getRedCards(player) + "",game.getFauls(player)+""});
                }else{
                    data.add(new String[]{"","", "","","","", game.getGuestTeamPlayers().get(counter1).getName(), game.getGoals(player) + ""
                            , game.getAssist(player) + "", game.getYellowCards(player) + "", game.getRedCards(player) + "",game.getFauls(player)+""});
                }
                counter1++;
            }
            data.add(new String[]{" "});
        }
            csvWriter.writeAll(data);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        activity.startActivity(intent);

        try {
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
