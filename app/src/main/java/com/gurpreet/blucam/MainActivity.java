package com.gurpreet.blucam;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gurpreet.blucam.components.dialog.DialogManager;
import com.gurpreet.blucam.connection.ClientThread;
import com.gurpreet.blucam.connection.ServerThread;
import com.gurpreet.blucam.constants.ToastConstants;
import com.gurpreet.blucam.util.BluetoothUtil;

import static android.bluetooth.BluetoothAdapter.ACTION_DISCOVERY_FINISHED;
import static android.bluetooth.BluetoothAdapter.ACTION_DISCOVERY_STARTED;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_ENABLE_BT = 1001;
    private static final int DISCOVERABLE_DURATION = 300;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1002;
    BluetoothAdapter mBluetoothAdapter;

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.e(TAG, "onReceive: action " + action);
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address

                Log.d(TAG, "onReceive: device name " + deviceName);
                Log.d(TAG, "onReceive: device mac address " + deviceHardwareAddress);
            } else if (ACTION_DISCOVERY_STARTED.equals(action)) {
                Log.d(TAG, "onReceive: discovery started");
            } else if (ACTION_DISCOVERY_FINISHED.equals(action)) {
                Log.d(TAG, "onReceive: discovery stopped");
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  enable bluetoooth device
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (BluetoothUtil.isBluetoothSupported(mBluetoothAdapter)) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            } else {
                initializeReceiver();
            }
        } else {
            Toast.makeText(this, ToastConstants.BLUETOOTH_NOT_SUPPORTED, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onPause() {
        if (mBluetoothAdapter != null) {
            if (mBluetoothAdapter.isDiscovering()) {
                mBluetoothAdapter.cancelDiscovery();
            }
        }
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult: request code " + requestCode);
        Log.e(TAG, "onActivityResult: result code " + resultCode);

        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d(TAG, "onActivityResult: request code matched " + requestCode);
                initializeReceiver();

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, ToastConstants.ERR_ENABLING_BLETOOTH, Toast.LENGTH_SHORT).show();
            }
        } else {
            if (resultCode == DISCOVERABLE_DURATION) {
                Log.d(TAG, "onActivityResult: discoverable request code matched " + requestCode);
                Toast.makeText(this, "Device is now discoverable", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, ToastConstants.ERR_DISCOVERING_BLUETOOTH, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initializeReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, filter);
    }

    private void makeBluetoothDiscoverable() {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DISCOVERABLE_DURATION);
        startActivity(discoverableIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // unregister the ACTION_FOUND receiver.
        unregisterReceiver(mReceiver);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    //////////////////////////////////////////////////////////////////
                    // circularPB.setVisibility(View.VISIBLE);
                    startDiscoveringDevices();
                } else {
                    Log.d(TAG, "onRequestPermissionsResult: permission denied");
                    //////////////////////////////////////////////////////////////////////
                    // circularPB.setVisibility(View.GONE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
                            DialogManager.showLocationPermissionDialog(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                                    LOCATION_PERMISSION_REQUEST_CODE);
                            Log.d(TAG, "onClick: should allow permission");
                        }
                        else {
                            Toast.makeText(this, "Please allow location permission in settings to discover " +
                                    "nearby bluetooth devices", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }
    }

    public void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.buttonSearch:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                            LOCATION_PERMISSION_REQUEST_CODE);
                } else {
                    startDiscoveringDevices();
                }
                break;
            case R.id.buttonListen:
                makeBluetoothDiscoverable();
                ///if(!BluetoothUtil.isDevicePaired(mBluetoothAdapter)) {
                ////}
                ////new ServerThread(mBluetoothAdapter).run();
                break;
        }
    }

    private void startDiscoveringDevices() {
        if(mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        mBluetoothAdapter.startDiscovery();
        //BluetoothClass.Device device = BluetoothDevice.get
        //new ClientThread(device, mBluetoothAdapter).run();
    }
}
