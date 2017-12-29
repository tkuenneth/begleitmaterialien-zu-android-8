package com.thomaskuenneth.bluetoothchatdemo;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

class ConnectThread extends MyThread {

    private static final String TAG = ConnectThread.class.getSimpleName();

    private final BluetoothSocket socket;

    ConnectThread(BluetoothDevice device, UUID uuid) {
        BluetoothSocket tmp = null;
        try {
            tmp = device.createRfcommSocketToServiceRecord(uuid);
        } catch (IOException e) {
            Log.e(TAG, "createRfcommSocketToServiceRecord() failed", e);
        }
        socket = tmp;
    }

    public void run() {
        try {
            socket.connect();
        } catch (IOException connectException) {
            try {
                socket.close();
            } catch (IOException closeException) {
                Log.e(TAG, "Could not close client socket", closeException);
            }
        }
    }

    public BluetoothSocket getSocket() {
        return socket;
    }
}
