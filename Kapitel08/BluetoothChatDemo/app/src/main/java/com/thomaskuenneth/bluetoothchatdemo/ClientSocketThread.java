package com.thomaskuenneth.bluetoothchatdemo;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

class ClientSocketThread extends SocketThread {

    private static final String TAG = ClientSocketThread.class.getSimpleName();

    private BluetoothSocket socket;

    ClientSocketThread(BluetoothDevice device, UUID uuid) {
        try {
            socket = device.createRfcommSocketToServiceRecord(uuid);
        } catch (IOException e) {
            Log.e(TAG, "createRfcommSocketToServiceRecord() failed", e);
            socket = null;
        }
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
