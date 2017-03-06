package com.gurpreet.blucam.util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.gurpreet.blucam.BlucamApplication;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Gurpreet on 04-03-2017.
 */

public class BluetoothUtil {

    private static final String TAG = BluetoothUtil.class.getSimpleName();

    /**
     * to check if bluetooth is supported by device
     *
     * @param mBluetoothAdapter
     * @return
     */
    public static boolean isBluetoothSupported(BluetoothAdapter mBluetoothAdapter) {
        return mBluetoothAdapter != null;
    }

    public static void makeBluetoothDiscoverable(Context context, int duration) {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, duration);
        discoverableIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        discoverableIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        context.startActivity(discoverableIntent);
    }

    /**
     * to check if device ia already paired
     *
     * @param mBluetoothAdapter
     * @return
     */
    public static ArrayList<BluetoothDevice> getPairedDevices(BluetoothAdapter mBluetoothAdapter) {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        ArrayList<BluetoothDevice> mPairedDevices = new ArrayList<>();

        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                mPairedDevices.add(device);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                Log.d(TAG, "isDevicePaired: name "+deviceName);
                Log.e(TAG, "isDevicePaired: mac address "+deviceHardwareAddress);
               // if(equal to mac address in sharedprefs) {
                //    return true;
               // }
            }
        }
        return mPairedDevices;
    }
}
