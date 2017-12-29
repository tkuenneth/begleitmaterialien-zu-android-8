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

    public void run() {
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

    public BluetoothSocket getSocket() {
        return socket;
    }
}
