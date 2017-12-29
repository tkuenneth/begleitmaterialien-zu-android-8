package com.thomaskuenneth.bluetoothchatdemo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

class ServerSocketThread extends SocketThread {

    private static final String TAG = ServerSocketThread.class.getSimpleName();

    private BluetoothServerSocket serverSocket;
    private BluetoothSocket socket;

    ServerSocketThread(BluetoothAdapter adapter, String name, UUID uuid) {
        socket = null;
        try {
            serverSocket = adapter.listenUsingRfcommWithServiceRecord(name, uuid);
        } catch (IOException e) {
            Log.e(TAG, "listenUsingRfcommWithServiceRecord() failed", e);
        }
    }

    @Override
    public void run() {
        boolean keepRunning = true;
        while (keepRunning) {
            try {
                socket = serverSocket.accept();
                if (socket != null) {
                    serverSocket.close();
                    keepRunning = false;
                }
            } catch (IOException e) {
                Log.e(TAG, "accept() failed", e);
                keepRunning = false;
            }
        }
    }

    @Override
    public BluetoothSocket getSocket() {
        return socket;
    }

    @Override
    public void cancel() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() failed", e);
            }
        }
    }
}
