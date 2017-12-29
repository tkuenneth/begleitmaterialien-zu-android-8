package com.thomaskuenneth.bluetoothchatdemo;

import android.bluetooth.BluetoothSocket;

public abstract class SocketThread extends Thread {

    public abstract BluetoothSocket getSocket();
}
