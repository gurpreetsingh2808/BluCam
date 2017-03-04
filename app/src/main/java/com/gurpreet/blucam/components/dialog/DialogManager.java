package com.gurpreet.blucam.components.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.gurpreet.blucam.R;
import com.gurpreet.blucam.constants.DialogConstants;
import com.gurpreet.blucam.ui.adapter.AvailableDevicesAdapter;

import java.util.ArrayList;

/**
 * Created by Gurpreet on 04-03-2017.
 */

public class DialogManager extends BaseDialogs{

    public static void showLocationPermissionDialog(final Activity activity, final String[] permissions, final int requestCode) {
        showPermissionDialog(activity, DialogConstants.BLUETOOTH_LOCATION_DESC, permissions, requestCode);
    }

    public static void showBluetoothDevicesDialog(Activity activity, AvailableDevicesAdapter.ItemListener itemListener,
                                                  ArrayList<BluetoothDevice> bluetoothDevices) {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_bluetooth_devices, null);
        alertDialog.setView(dialogView);
        final AlertDialog reportDialog = alertDialog.create();

        TextView tvNoDevice = (TextView) dialogView.findViewById(R.id.tvNoDevice);
        RecyclerView rvDevices = (RecyclerView) dialogView.findViewById(R.id.rvDevices);
        rvDevices.setLayoutManager(new LinearLayoutManager(activity));
        if(bluetoothDevices.size() > 0) {
            AvailableDevicesAdapter mAdapter = new AvailableDevicesAdapter(bluetoothDevices,
                    itemListener, activity);
            rvDevices.setAdapter(mAdapter);
        }
        else {
            tvNoDevice.setVisibility(View.VISIBLE);
        }

        reportDialog.show();
        reportDialog.getWindow().setLayout(activity.getResources().getDimensionPixelSize(R.dimen.popup_width),
                ViewGroup.LayoutParams.WRAP_CONTENT);



    }

}
