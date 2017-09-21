package com.apress.gerber.footballman;

import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * Created by hriso on 8/29/2017.
 */

public class AlertDialogButtons implements DialogInterface.OnClickListener {
    private AlertDialog alert;

    AlertDialogButtons(AlertDialog alert) {
        this.alert = alert;
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        alert.cancel();
    }
}
