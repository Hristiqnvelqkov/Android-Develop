package com.apress.gerber.footballman;

import android.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;

import com.apress.gerber.footballman.Fragments.HomeFragment;
import com.apress.gerber.footballman.Fragments.StartMatchFragment;

import utils.StoreGameInPrefenreces;

/**
 * Created by hriso on 10/1/2017.
 */

public class GoToHomeDialog {

    private AlertDialog mAlert;
    private MainActivity mActivity;
    private StartMatchFragment mStartMatchFragment;
    public GoToHomeDialog(final MainActivity activity, StartMatchFragment fragment) {
        mActivity = activity;
        mStartMatchFragment = fragment;
        mAlert = new AlertDialog.Builder(activity).create();
        mAlert.setTitle("Are you sure to exit the game");
        mAlert.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mStartMatchFragment.stopTask();
                StoreGameInPrefenreces.erasePreferences(activity);
                activity.getSupportActionBar().setTitle("Football Manager");
                activity.restartActivity();
            }
        });
        mAlert.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new AlertDialogButtons(mAlert));
        mAlert.show();
    }

}
