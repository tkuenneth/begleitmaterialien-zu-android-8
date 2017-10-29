package com.thomaskuenneth.broadcastreceiverdemo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class BroadcastReceiverDemo extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toast.makeText(this, R.string.info,
                Toast.LENGTH_LONG).show();

        finish();
    }
}