package utils;

import android.support.v4.util.Pair;

import com.apress.gerber.footballman.Models.DataManager;
import com.apress.gerber.footballman.Models.League;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

/**
 * Created by hristiyan on 05.04.18.
 */

public class SynchronizeManager implements DownloadTask.DownloadedData {
    public static final int LEAGUES = 1;
    public static final int TEAMS_FOR_LEAGUE = 2;
    public static final int PLAYERS_FOR_TEAM = 3;
    private final static String TEAMS_URL = "https://spl-bg.eu/wp-json/sportspress/v2/teams";
    private final static String LEAGUES_URL = "https://spl-bg.eu/wp-json/sportspress/v2/leagues";
    private LinkedList<String> leaguesId = new LinkedList<>();
    private LinkedList<String> teamsId = new LinkedList<>();
    private static final SynchronizeManager ourInstance = new SynchronizeManager();

    public static SynchronizeManager getInstance() {
        return ourInstance;
    }

    private SynchronizeManager() {
    }

    public void downLoadLeagues() {
        DownloadTask leaguesTask = new DownloadTask(this);
        Pair<String, Integer> pair = new Pair<>(LEAGUES_URL, LEAGUES);
        leaguesTask.execute(pair);
    }

    public void downloadTeams() {

    }

    private void parseLeaguesResponse(StringBuffer buffer) {
        String response = buffer.toString();
        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject object = (JSONObject) jsonArray.get(i);
                DataManager.getDataInstance().addLeague(object.getString("slug"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dataIsReady(StringBuffer response, int type) {
        if (type == LEAGUES) {
            parseLeaguesResponse(response);
        }
    }
}
