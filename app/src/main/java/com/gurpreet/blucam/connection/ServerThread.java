package com.gurpreet.blucam.connection;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.gurpreet.blucam.BuildConfig;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Gurpreet on 04-03-2017.
 */

public class ServerThread extends Thread {

    private static final String TAG = ServerThread.class.getSimpleName();
    private final BluetoothServerSocket mmServerSocket;

        public ServerThread(BluetoothAdapter mBluetoothAdapter) {
            // Use a temporary object that is later assigned to mmServerSocket
            // because mmServerSocket is final.
            BluetoothServerSocket tmp = null;
            try {
                // MY_UUID is the app's UUID string, also used by the client code.
                UUID serviceUUID = UUID.fromString(BuildConfig.CUSTOM_UUID);
                String deviceName = android.os.Build.MANUFACTURER + " " +android.os.Build.PRODUCT;
                Log.e(TAG, "ServerThread: "+deviceName );
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(deviceName, serviceUUID);
            } catch (IOException e) {
                Log.e(TAG, "Socket's listen() method failed", e);
            }
            mmServerSocket = tmp;
        }

        public void run() {
            BluetoothSocket socket = null;
            // Keep listening until exception occurs or a socket is returned.
            while (true) {
                try {
                    socket = mmServerSocket.accept();


                if (socket != null) {
                    // A connection was accepted. Perform work associated with
                    // the connection in a separate thread.
                    ////////////////////////////////
                    // //manageMyConnectedSocket(socket);
                    mmServerSocket.close();
                    break;
                }
                } catch (IOException e) {
                    Log.e(TAG, "Socket's accept() method failed", e);
                    break;
                }
            }
        }

        // Closes the connect socket and causes the thread to finish.
        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connect socket", e);
            }
        }
}
