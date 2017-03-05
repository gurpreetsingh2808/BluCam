package com.gurpreet.blucam.components.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.ActivityCompat;


/**
 * Created by Gurpreet on 04-03-2017.
 */

public class BaseDialogs {

    protected static void showPermissionDialog(final Activity activity, String description, final String[] permissions, final int requestCode) {
        new AlertDialog.Builder(activity)
                .setTitle(DialogConstants.PERMISSION_REQUIRED)
                .setMessage(description)
                .setPositiveButton(DialogConstants.ASK_AGAIN, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(activity, permissions, requestCode);
                    }
                })
                .setNegativeButton(DialogConstants.DENY, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .create().show();
    }

    public static void showErrorDialog(final Activity activity, final int requestCode) {
        new AlertDialog.Builder(activity)
                .setTitle(DialogConstants.ERROR)
                //.setMessage(errorData.getMessage())
                .setPositiveButton(DialogConstants.ASK_AGAIN, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton(DialogConstants.OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .create().show();
    }
}
