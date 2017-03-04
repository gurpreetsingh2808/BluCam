package com.gurpreet.blucam.components.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.ActivityCompat;

import com.gurpreet.blucam.constants.DialogConstants;

/**
 * Created by Gurpreet on 04-03-2017.
 */

public class DialogManager extends BaseDialogs{

    public static void showLocationPermissionDialog(final Activity activity, final String[] permissions, final int requestCode) {
        showPermissionDialog(activity, DialogConstants.BLUETOOTH_LOCATION_DESC, permissions, requestCode);
    }


}
