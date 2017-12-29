package com.thomaskuenneth.bluetoothchatdemo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

class ServerThread extends MyThread {

    private static final String TAG = ServerThread.class.getSimpleName();

    private final BluetoothServerSocket serverSocket;

    private BluetoothSocket socket;

    ServerThread(BluetoothAdapter adapter, String name, UUID uuid) {
        BluetoothServerSocket tmp = null;
        try {
            tmp = adapter.listenUsingRfcommWithServiceRecord(name, uuid);
        } catch (IOException e) {
            Log.e(TAG, "listenUsingRfcommWithServiceRecord() failed", e);
        }
        serverSocket = tmp;
    }

    public BluetoothSocket getSocket() {
        return socket;
    }

    public void run() {
        socket = null;
        while (true) {
            try {
                socket = serverSocket.accept();
                if (socket != null) {
                    serverSocket.close();
                    break;
                }
            } catch (IOException e) {
                Log.e(TAG, "accept() failed", e);
                break;
            }
        }
    }
}
