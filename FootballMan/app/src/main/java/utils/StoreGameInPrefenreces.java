package utils;

import android.app.Activity;

import com.apress.gerber.footballman.MainActivity;
import com.apress.gerber.footballman.Models.Game;
import com.google.gson.Gson;

/**
 * Created by hristiyan on 09.03.18.
 */

public class StoreGameInPrefenreces {
    public static String GAME = "GAME";

    public static void writeToPreferences(MainActivity activity, Game game){
        Gson gson = new Gson();
        String gameGson = gson.toJson(game);
        activity.getPreferences().edit().putString(GAME, gameGson).commit();
    }

    public static Game getGameFromPreferences(MainActivity activity){
        Gson gameGson = new Gson();
        String gameJson = activity.getPreferences().getString(GAME,null);
        Game game = gameGson.fromJson(gameJson,Game.class);
        return game;
    }

    public static void erasePreferences(MainActivity activity){
        activity.getPreferences().edit().remove(GAME).apply();
    }
}
