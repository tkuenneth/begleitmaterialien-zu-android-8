package com.thomaskuenneth.preferencesdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

public class PreferencesDemoActivity extends Activity {

    private static final String TAG =
            PreferencesDemoActivity.class.getSimpleName();

    private static final int RQ_SETTINGS = 1234;

    private TextView textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(PreferencesDemoActivity.this,
                    SettingsActivity.class);
            startActivityForResult(intent, RQ_SETTINGS);
        });
        textview = findViewById(R.id.textview);
        updateTextView();
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RQ_SETTINGS == requestCode) {
            updateTextView();
        }
    }

    private void updateTextView() {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        boolean cb1 = prefs.getBoolean("checkbox_1", false);
        boolean cb2 = prefs.getBoolean("checkbox_2", false);
        String et1 = prefs.getString("edittext_1", "");
        String _s = et1;
        if (et1.length() < 1) {
            _s = getString(R.string.empty);
        }
        textview.setText(getString(R.string.template,
                Boolean.toString(cb1), Boolean.toString(cb2), _s));

        try {
            if (!compareCurrentWithStoredVersionCode(this,
                    prefs)) {
                SharedPreferences.Editor e = prefs.edit();
                e.putBoolean("checkbox_1", cb1);
                e.putBoolean("checkbox_2", cb2);
                e.putString("edittext_1", et1);
                e.apply();
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "updateTextView()", e);
        }
    }

    public static boolean compareCurrentWithStoredVersionCode(
            Context context,
            SharedPreferences prefs) throws PackageManager.NameNotFoundException {
        int currentVersionCode;
        PackageInfo info = context.getPackageManager().getPackageInfo(
                context.getPackageName(), 0);
        currentVersionCode = info.versionCode;
        int lastStored = prefs.getInt("storedVersionCode", -1);
        if (lastStored != currentVersionCode) {
            SharedPreferences.Editor e = prefs.edit();
            e.putInt("storedVersionCode", currentVersionCode);
            e.apply();
            return false;
        }
        return true;
    }
}