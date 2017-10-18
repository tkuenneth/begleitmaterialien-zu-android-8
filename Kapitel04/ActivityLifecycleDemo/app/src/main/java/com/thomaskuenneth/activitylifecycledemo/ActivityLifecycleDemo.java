package com.thomaskuenneth.activitylifecycledemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

public class ActivityLifecycleDemo extends Activity {

    private static final String TAG =
            ActivityLifecycleDemo.class.getSimpleName();

    private static int zaehler = 1;

    private int lokalerZaehler = zaehler++;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        log("onCreate");
        setContentView(R.layout.main);
        TextView tv = findViewById(R.id.textview);
        tv.setText(getString(R.string.msg, lokalerZaehler));

        Button buttonNew = findViewById(R.id.id_new);
        buttonNew.setOnClickListener(v -> {
            Intent i = new Intent(ActivityLifecycleDemo.this,
                    ActivityLifecycleDemo.class);
            startActivity(i);
        });

        Button buttonFinish = findViewById(R.id.id_finish);
        buttonFinish.setOnClickListener(v -> finish());
    }

    @Override
    protected void onStart() {
        super.onStart();
        log("onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        log("onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        log("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        log("onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        log("onDestroy");
    }

    private void log(String methodName) {
        Log.d(TAG, methodName + "() #" + lokalerZaehler);
    }
}
