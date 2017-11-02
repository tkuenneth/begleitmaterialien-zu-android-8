package com.thomaskuenneth.uithreaddemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class UIThreadDemo extends Activity {

    public static final String TAG = UIThreadDemo.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final TextView tv = findViewById(R.id.textview);
        final CheckBox checkbox = findViewById(R.id.checkbox);
        checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> tv.setText(Boolean.toString(isChecked)));
        checkbox.setChecked(true);
        final Button button = findViewById(R.id.button);

        button.setOnClickListener(v -> {
            // ursprüngliche Version
            tv.setText(UIThreadDemo.this.getString(R.string.begin));
            if (checkbox.isChecked()) {
                try {
                    Thread.sleep(3500);
                } catch (InterruptedException e) {
                    Log.e(TAG, "sleep()", e);
                }
            } else {
                while (true) ;
            }
            tv.setText(UIThreadDemo.this.getString(R.string.end));

            // //fehlerhafteVersion
            new Thread(() -> {
                tv.setText(UIThreadDemo.this.getString(R.string.begin));
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    Log.e(TAG, "sleep()", e);
                }
                tv.setText(UIThreadDemo.this.getString(R.string.end));
            }).start();

            // korrekte Version
            final Handler h = new Handler();
            new Thread(() -> {
                try {
                    h.post(() -> tv.setText(UIThreadDemo.this
                            .getString(R.string.begin)));
                    Thread.sleep(10000);
                    h.post(() -> tv.setText(UIThreadDemo.this
                            .getString(R.string.end)));
                } catch (InterruptedException e) {
                    Log.e(TAG, "sleep()", e);
                }
            }).start();

            // ebenfalls korrekte Version
            Thread t = new Thread(() -> {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    Log.e(TAG, "sleep()", e);
                }
                runOnUiThread(() -> tv.setText(UIThreadDemo.this.getString(
                        R.string.end)));
            });
            tv.setText(UIThreadDemo.this.getString(R.string.begin));
            t.start();
        });
    }
}
