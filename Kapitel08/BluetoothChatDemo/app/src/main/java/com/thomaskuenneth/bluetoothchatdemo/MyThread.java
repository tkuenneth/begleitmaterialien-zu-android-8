package com.thomaskuenneth.bluetoothchatdemo;

import android.bluetooth.BluetoothSocket;

public abstract class MyThread extends Thread {

    public abstract BluetoothSocket getSocket();
}
