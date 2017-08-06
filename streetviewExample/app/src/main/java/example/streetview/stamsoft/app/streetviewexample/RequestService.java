package example.streetview.stamsoft.app.streetviewexample;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

import example.streetview.stamsoft.app.streetviewexample.fragments.HomeFragment;

/**
 * Created by hriso on 8/5/2017.
 */

public class RequestService extends IntentService {
    public final static String BROADCAST_ACTION = "example.streetview.stamsoft.app.streetviewexample";
    public final static String EXTRA="Location";
    public RequestService(String name) {
        super(name);
    }
    public RequestService(){
        super("");

    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String city = intent.getStringExtra(HomeFragment.STRING_EXTRA);
        final String ENDPOINT = "https://maps.googleapis.com/maps/api/place/textsearch/json?query="+city+"&key=%20AIzaSyDoJy3_RKtxwAVfBtHj7kZbsGbJRusdruc";
        try {
            URL url = new URL(ENDPOINT);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            String inputLine;
            StringBuffer buffer=new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            while((inputLine=reader.readLine())!=null){
                buffer.append(inputLine);
            }
            JSONObject barsJson = new JSONObject(buffer.toString());
            JSONArray jsonArray = (barsJson.getJSONArray("results"));
            Intent broadcast = new Intent();
            broadcast.setAction(BROADCAST_ACTION);
            if (jsonArray.length()>0) {
                JSONObject explrObject = jsonArray.getJSONObject(0);
                String LatLang = ((explrObject.get("geometry")).toString()).split(Pattern.quote("}"))[0];
                LatLang = LatLang.split(Pattern.quote("{"))[2];
                String Lat = LatLang.split(",")[0];
                Lat = Lat.split(":")[1];
                String Lag = LatLang.split(",")[1];
                Lag = Lag.split(":")[1];
                broadcast.putExtra(EXTRA, Lat +","+ Lag + "");
            }else{
                broadcast.putExtra(EXTRA, "");
            }
            LocalBroadcastManager.getInstance(this).sendBroadcast(broadcast);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
