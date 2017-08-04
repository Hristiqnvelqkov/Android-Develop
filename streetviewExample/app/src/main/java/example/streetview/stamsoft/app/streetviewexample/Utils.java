package example.streetview.stamsoft.app.streetviewexample;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import example.streetview.stamsoft.app.streetviewexample.data.BleData;
import example.streetview.stamsoft.app.streetviewexample.interfaces.Constants;

public class Utils {

    private static int mCurrentValue = 0;

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static double[] getValuesByTabletType(int tabletType, double value) {
        double[] results = new double[4];

        Log.d("Device1", "value " + value);
        Log.d("Device1", "tabletType " + tabletType);

        switch (tabletType) {
            case 1:
                results[0] = (value * Constants.QOEF_TAB_1);
                break;
            case 2:
                results[0] = value * Constants.QOEF_TAB_2;
                break;
            case 3:
                results[0] = value * Constants.QOEF_TAB_3;
                break;
        }

        return results;
    }

    public static void saveDevices(List<String> devices, Context context) {
        String devicesToSave = "";
        for (int i = 0; i < devices.size(); ++i) {
            devicesToSave += devices.get(i);
            if (i < devices.size() - 1) {
                devicesToSave += ",";
            }
        }
        PreferenceManager.getDefaultSharedPreferences(context).edit().
                putString(context.getString(R.string.pref_items), devicesToSave).commit();
    }

    public static List<String> getSavedDevice(Context context) {
        List<String> devices = new ArrayList<>();
        String pref = PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.pref_items), "");
        String[] splitted = pref.split(",");
        for (String dev : splitted) {
            if (!TextUtils.isEmpty(dev)) {
                devices.add(dev);
            }
        }

        return devices;
    }

    public static int getCurrentProgress(Map<Integer, BleData> bleDatas, int maxValue) {
        double value = 0;
        if (bleDatas != null) {
            for (Integer key : bleDatas.keySet()) {
                value += bleDatas.get(key).getPoints();
            }
            value = getValuesByTabletType(1, value)[0];
        }
        mCurrentValue += (int) value;
        if (mCurrentValue >= maxValue) {
            mCurrentValue = 0;
            return 0;
        }
        return mCurrentValue;
    }
}
