package utils;

import android.os.AsyncTask;
import android.support.v4.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by hristiyan on 05.04.18.
 */

public class DownloadTask extends AsyncTask<Pair<String, Integer>, Void, Pair<StringBuffer, Integer>> {

    DownloadedData listner;

    DownloadTask(DownloadedData listner) {
        this.listner = listner;
    }

    @Override
    protected Pair<StringBuffer, Integer> doInBackground(Pair<String, Integer>... pairs) {
        String urlString = pairs[0].first;
        int type = pairs[0].second;
        Pair<StringBuffer, Integer> pair;
        StringBuffer response = new StringBuffer();
        try {
            URL url = new URL(urlString);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;
            while ((inputLine = bufferedReader.readLine()) != null) {
                response.append(inputLine);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        pair = new Pair<>(response, type);
        return pair;
    }

    @Override
    protected void onPostExecute(Pair<StringBuffer, Integer> response) {
        super.onPostExecute(response);
        listner.dataIsReady(response.first, response.second);
    }

    public interface DownloadedData {
        void dataIsReady(StringBuffer response, int type);
    }
}
