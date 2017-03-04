package com.gurpreet.blucam.util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.util.Log;

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


    /**
     * to check if device ia already paired
     *
     * @param mBluetoothAdapter
     * @return
     */
    public static Boolean isDevicePaired(BluetoothAdapter mBluetoothAdapter) {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                Log.d(TAG, "isDevicePaired: name "+deviceName);
                Log.e(TAG, "isDevicePaired: mac address "+deviceHardwareAddress);
               // if(equal to mac address in sharedprefs) {
                //    return true;
               // }
            }
        }
        return false;
    }
}
