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
        socket = null;
        try {
            socket = device.createRfcommSocketToServiceRecord(uuid);
        } catch (IOException e) {
            Log.e(TAG, "createRfcommSocketToServiceRecord() failed", e);
        }
    }

    @Override
    public void run() {
        try {
            socket.connect();
        } catch (IOException connectException) {
            cancel();
        }
    }

    @Override
    public BluetoothSocket getSocket() {
        return socket;
    }

    @Override
    public void cancel() {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close client socket", e);
            } finally {
                socket = null;
            }
        }
    }
}
