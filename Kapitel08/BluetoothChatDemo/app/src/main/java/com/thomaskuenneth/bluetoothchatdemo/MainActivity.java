package com.thomaskuenneth.bluetoothchatdemo;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_ACCESS_COARSE_LOCATION
            = 321;
    private static final String GERAET_1 = "Pixel 2";
    private static final String GERAET_2 = "Nexus 5X";
    private static final UUID MY_UUID = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");

    private EditText input;
    private TextView output;

    private BluetoothAdapter adapter;

    private ServerThread serverThread;
    private ConnectThread connectThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        input = findViewById(R.id.input);
        input.setEnabled(false);
        output = findViewById(R.id.output);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter = null;
        if (checkSelfPermission(
                Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]
                            {Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_ACCESS_COARSE_LOCATION);
        } else {
            startOrFinish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        if ((requestCode == REQUEST_ACCESS_COARSE_LOCATION) &&
                (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            startOrFinish();
        }
    }

    private void startOrFinish() {
        if (isBluetoothEnabled()) {
            connect();
        }
    }

    private boolean isBluetoothEnabled() {
        adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null) {
            if (!adapter.isEnabled()) {
                Toast.makeText(this, R.string.enable_bluetooth, Toast.LENGTH_LONG)
                        .show();
                finish();
                return false;
            }
        }
        return true;
    }

    private void connect() {
        String myName = adapter.getName();
        String otherName = GERAET_1.equals(myName) ? GERAET_2 : GERAET_1;
        BluetoothDevice remoteDevice = null;
        Set<BluetoothDevice> devices = adapter.getBondedDevices();
        for (BluetoothDevice device : devices) {
            if (otherName.equals(device.getName())) {
                remoteDevice = device;
            }
        }
        if (remoteDevice != null) {
            serverThread = new ServerThread(adapter, TAG, MY_UUID);
            serverThread.start();
            Thread t1 = createThread(serverThread);
            t1.start();
            connectThread = new ConnectThread(remoteDevice, MY_UUID);
            connectThread.start();
            Thread t2 = createThread(connectThread);
            t2.start();
            input.setEnabled(true);
        }
    }

    private void send(OutputStream os, String text) {
        try {
            os.write(text.getBytes());
        } catch (IOException e) {
            Log.e(TAG, "error while sending", e);
        }
    }

    private Thread createThread(MyThread t) {
        return new Thread(() -> {
            try {
                t.join();
                BluetoothSocket socket = t.getSocket();
                if (socket != null) {
                    input.setOnEditorActionListener((view, actionId, event) -> {
                        try {
                            OutputStream os = socket.getOutputStream();
                            send(os, input.getText().toString() + "\n");
                            input.setText("");
                        } catch (IOException e) {
                            Log.e(TAG, null, e);
                        }
                        return true;
                    });
                    InputStream is = socket.getInputStream();
                    while (true) {
                        try {
                            int i = is.read();
                            if (i != -1) {
                                runOnUiThread(() -> {
                                    output.setText(output.getText().toString() + (char) i);
                                });
                            }
                        } catch (IOException e) {
                            Log.e(TAG, null, e);
                        }
                    }
                }
            } catch (InterruptedException | IOException e) {
                Log.e(TAG, null, e);
            }
        });
    }
}
