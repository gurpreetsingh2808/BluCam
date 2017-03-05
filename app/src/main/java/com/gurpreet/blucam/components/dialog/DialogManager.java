package com.gurpreet.blucam.components.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gurpreet.blucam.R;
import com.gurpreet.blucam.ui.adapter.AvailableDevicesAdapter;

import java.util.ArrayList;

/**
 * Created by Gurpreet on 04-03-2017.
 */

public class DialogManager extends BaseDialogs {

    public static void showLocationPermissionDialog(final Activity activity, final String[] permissions, final int requestCode) {
        showPermissionDialog(activity, DialogConstants.BLUETOOTH_LOCATION_DESC, permissions, requestCode);
    }

}
