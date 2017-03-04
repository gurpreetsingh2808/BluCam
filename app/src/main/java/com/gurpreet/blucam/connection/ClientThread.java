package com.gurpreet.blucam.connection;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.gurpreet.blucam.BuildConfig;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Gurpreet on 04-03-2017.
 */

public class ClientThread extends Thread {

    private static final String TAG = ClientThread.class.getSimpleName();
    private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
    private BluetoothAdapter mBluetoothAdapter;

        public ClientThread(BluetoothDevice device, BluetoothAdapter mBluetoothAdapter) {
            // Use a temporary object that is later assigned to mmSocket
            // because mmSocket is final.
            BluetoothSocket tmp = null;
            mmDevice = device;
            this.mBluetoothAdapter = mBluetoothAdapter;

            try {
                // Get a BluetoothSocket to connect with the given BluetoothDevice.
                // MY_UUID is the app's UUID string, also used in the server code.
                UUID serviceUUID = UUID.fromString(BuildConfig.CUSTOM_UUID);
                tmp = device.createRfcommSocketToServiceRecord(serviceUUID);
            } catch (IOException e) {
                Log.e(TAG, "Socket's create() method failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it otherwise slows down the connection.
            mBluetoothAdapter.cancelDiscovery();

            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and return.
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    Log.e(TAG, "Could not close the client socket", closeException);
                }
                return;
            }

            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
            ///////////////////////////////////
            // manageMyConnectedSocket(mmSocket);
        }

        // Closes the client socket and causes the thread to finish.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the client socket", e);
            }
        }
}
