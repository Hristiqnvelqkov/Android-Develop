package com.example.hristiyan.menu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.example.hristiyan.menu.data.Food;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by hristiyan on 07.02.18.
 */

public class Tools {

    public static String getSimpleDate(long timeStamp) {
        Date date = new Date(timeStamp);
        DateFormat formatter = new SimpleDateFormat("yyyy:MM:dd HH:mm", Locale.getDefault());
        return formatter.format(date);
    }
    public static void showAlertDialog(Context context,String message, DialogInterface.OnClickListener onYesListner){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setPositiveButton(R.string.yes, onYesListner);
        builder.setMessage(message)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        builder.create();
        builder.show();
    }
    public static int generatePic(Context context){
        Random rand = new Random();
        int n = rand.nextInt(5) + 1;
        int resourceID = context.getResources().getIdentifier("a"+String.valueOf(n), "drawable", context.getPackageName());
        return resourceID;
    }
}
