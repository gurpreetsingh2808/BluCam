package com.gurpreet.blucam;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
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
import android.widget.Toast;

import com.gurpreet.blucam.components.dialog.DialogManager;
import com.gurpreet.blucam.components.toast.ToastManager;
import com.gurpreet.blucam.components.toast.ToastConstants;
import com.gurpreet.blucam.service.BluetoothService;
import com.gurpreet.blucam.ui.activity.BluetoothCamActivity;
import com.gurpreet.blucam.ui.activity.DeviceListActivity;
import com.gurpreet.blucam.ui.adapter.AvailableDevicesAdapter;
import com.gurpreet.blucam.util.BluetoothUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;

import static android.bluetooth.BluetoothAdapter.ACTION_DISCOVERY_FINISHED;
import static android.bluetooth.BluetoothAdapter.ACTION_DISCOVERY_STARTED;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_ENABLE_BT = 1001;
    private static final int DISCOVERABLE_DURATION = 300;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1002;
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    // Return Intent extra
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    BluetoothAdapter mBluetoothAdapter;
    private ArrayList<BluetoothDevice> mDeviceList = new ArrayList<BluetoothDevice>();
    private String deviceAddress;
    private BluetoothService mBluService = null;


    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.e(TAG, "onReceive: action " + action);
            switch (action) {
                case BluetoothDevice.ACTION_FOUND:
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    mDeviceList.add(device);
                    String deviceName = device.getName();
                    String deviceHardwareAddress = device.getAddress(); // MAC address

                    Log.d(TAG, "onReceive: device name " + deviceName);
                    Log.d(TAG, "onReceive: device mac address " + deviceHardwareAddress);
                    break;
                case ACTION_DISCOVERY_STARTED:
                    Log.d(TAG, "onReceive: discovery started");
                    break;
                case ACTION_DISCOVERY_FINISHED:
                    Log.d(TAG, "onReceive: discovery stopped");
                    break;
                case BluetoothDevice.ACTION_BOND_STATE_CHANGED:
                    final int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
                    final int prevState	= intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR);

                    if (state == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_BONDING) {
                        ToastManager.showToast(ToastConstants.DEVICE_PAIRED);
                        startActivity(new Intent(MainActivity.this, BluetoothCamActivity.class));

                    } else if (state == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDED){
                        ToastManager.showToast(ToastConstants.DEVICE_UNPAIRED);
                    }
                    break;
                case BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED:
                    final int changedState = intent.getIntExtra(BluetoothAdapter.EXTRA_CONNECTION_STATE, BluetoothDevice.ERROR);
                    Log.e(TAG, "onReceive: changed state "+changedState);
                    if(changedState == 0) {
                        ToastManager.showToast(ToastConstants.DEVICE_DISCONNECTED);
                    }
                    ///////////////   NO USE HERE
                    else if(changedState == 2) {
                        ToastManager.showToast(ToastConstants.DEVICE_CONNECTED);

                    }
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
///////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult: request code " + requestCode);
        Log.e(TAG, "onActivityResult: result code " + resultCode);

        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d(TAG, "onActivityResult: request code matched " + requestCode);
                ToastManager.showToast(ToastConstants.ENABLED_BLETOOTH);
                initializeReceiver();

            } else if (resultCode == RESULT_CANCELED) {
                ToastManager.showToast(ToastConstants.ERR_ENABLING_BLETOOTH);
            }
        } else if (requestCode == REQUEST_CONNECT_DEVICE_SECURE) {
            // When DeviceListActivity returns with a device to connect
            if (resultCode == Activity.RESULT_OK) {
                Intent i = new Intent(MainActivity.this, BluetoothCamActivity.class);
                String address = data.getExtras().getString(MainActivity.EXTRA_DEVICE_ADDRESS);
                i.putExtra(EXTRA_DEVICE_ADDRESS, address);
                startActivity(i);
            }
        }

    }

    private void initializeReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mReceiver, filter);
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
                BluetoothUtil.makeBluetoothDiscoverable(this, DISCOVERABLE_DURATION);
                startActivity(new Intent(MainActivity.this, BluetoothCamActivity.class));
                break;
        }
    }

    private void startDiscoveringDevices() {
       /* mDeviceList.clear();
        if(mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        mBluetoothAdapter.startDiscovery();
        */
        // Launch the DeviceListActivity to see devices and do scan
        Intent serverIntent = new Intent(this, DeviceListActivity.class);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
    }

    private void pairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("createBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unpairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("removeBond", (Class[]) null);
            method.invoke(device, (Object[]) null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
