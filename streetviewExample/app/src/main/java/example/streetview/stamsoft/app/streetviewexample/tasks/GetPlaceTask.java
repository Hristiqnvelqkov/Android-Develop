package example.streetview.stamsoft.app.streetviewexample.tasks;

import android.os.AsyncTask;

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

/**
 * Created by hriso on 8/5/2017.
 */

public class GetPlaceTask extends AsyncTask<String,Void,LatLng> {
    @Override
    protected LatLng doInBackground(String... strings) {
        LatLng latLang=null;
        final String ENDPOINT = "https://maps.googleapis.com/maps/api/place/textsearch/json?query="+strings[0]+"&key=%20AIzaSyDoJy3_RKtxwAVfBtHj7kZbsGbJRusdruc";
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

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject explrObject = jsonArray.getJSONObject(i);
                String LatLang = ((explrObject.get("geometry")).toString()).split(Pattern.quote("}"))[0];
                LatLang = LatLang.split(Pattern.quote("{"))[2];
                String Lat = LatLang.split(",")[0];
                Lat = Lat.split(":")[1];
                String Lag = LatLang.split(",")[1];
                Lag = Lag.split(":")[1];
                latLang = new LatLng(Float.parseFloat(Lat),Float.parseFloat(Lag));
            }
            } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return latLang;
    }
}
