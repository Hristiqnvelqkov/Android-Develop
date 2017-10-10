package com.apress.gerber.footballman;

import android.Manifest;
import android.app.Activity;
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
    private CSVWriter csvWriter;

    private ExportData() {
    }

    public static ExportData getInstance() {
        return data;
    }

    public void writeToFile(String fileName, Activity activity, int exportType) {
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
                }
            }

        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    DataManager.getDataInstance().getLeagues(this);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
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
        data.add(new String[]{"TEAMS","RESULTS","OUTCOME", "PLAYERS", "GOALS", "ASSIST", "YELLOW CARDS", "RED CARDS"});
        for (Game game : games){
            int counter =0;
            for(Player player : game.getHostPlayers()){
                System.out.println(game.getGoals(player));
                data.add(new String[]{game.getHost().getName(),game.getTeamFirstHalfGoals(game.getHostPlayers())+"|"
                        +game.getTeamSecondHalfGoals(game.getHostPlayers())+"|"+game.getHostResult()
                        ,game.outCome(game.getHost()),game.getHostPlayers().get(counter).getName(),game.getGoals(player)+""
                        ,game.getAssist(player)+"",game.getYellowCards(player)+"",game.getRedCards(player)+""});

                counter++;
            }
            int counter1 =0;
            for(Player player : game.getGuestTeamPlayers()){
                System.out.println(game.getGoals(player));
                data.add(new String[]{game.getGuest().getName(),game.getTeamFirstHalfGoals(game.getGuestTeamPlayers())+"|"
                        +game.getTeamSecondHalfGoals(game.getGuestPlayersInGame())+"|"+game.getGuestResult()
                        ,game.outCome(game.getGuest()),game.getGuestTeamPlayers().get(counter1).getName(),game.getGoals(player)+""
                        ,game.getAssist(player)+"",game.getYellowCards(player)+"",game.getRedCards(player)+""});

                counter1++;
            }
            data.add(new String[]{" "});
        }
            csvWriter.writeAll(data);
        try {
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
