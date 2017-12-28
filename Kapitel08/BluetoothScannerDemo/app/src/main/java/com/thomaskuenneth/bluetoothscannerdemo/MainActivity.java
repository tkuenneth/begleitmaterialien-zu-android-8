package com.thomaskuenneth.bluetoothscannerdemo;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import java.util.Set;

public class MainActivity extends Activity {

    private static final int REQUEST_ENABLE_BT = 123;
    private static final int REQUEST_ACCESS_COARSE_LOCATION
            = 321;

    private BluetoothAdapter adapter;
    private TextView tv;
    private boolean started;

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                tv.append(getString(R.string.template,
                        device.getName(),
                        device.getAddress()));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.tv);
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter = null;
        started = false;
        if (checkSelfPermission(
                Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]
                            {Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_ACCESS_COARSE_LOCATION);
        } else {
            if (isBluetoothEnabled()) {
                showDevices();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (started) {
            adapter.cancelDiscovery();
            started = false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        if ((requestCode == REQUEST_ACCESS_COARSE_LOCATION) &&
                (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            if (isBluetoothEnabled()) {
                showDevices();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((resultCode == RESULT_OK) && (requestCode == REQUEST_ENABLE_BT)) {
            showDevices();
        }
    }

    private boolean isBluetoothEnabled() {
        boolean enabled = false;
        adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null) {
            enabled = adapter.isEnabled();
            if (!enabled) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
        return enabled;
    }

    private void showDevices() {
        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.paired));
        Set<BluetoothDevice> devices = adapter.getBondedDevices();
        for (BluetoothDevice device : devices) {
            sb.append(getString(R.string.template,
                    device.getName(),
                    device.getAddress()));
        }
        sb.append("\n");
        if (started) {
            adapter.cancelDiscovery();
        }
        started = adapter.startDiscovery();
        if (started) {
            sb.append(getString(R.string.others));
        }
        tv.setText(sb.toString());
    }
}
