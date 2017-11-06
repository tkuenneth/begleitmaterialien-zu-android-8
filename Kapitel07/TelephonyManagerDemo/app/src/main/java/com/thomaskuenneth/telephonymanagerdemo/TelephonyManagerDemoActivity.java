package com.thomaskuenneth.telephonymanagerdemo;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

public class TelephonyManagerDemoActivity extends Activity {

    private static final String TAG =
            TelephonyManagerDemoActivity.class.getSimpleName();

    private static final int RQ_READ_PHONE_STATE =
            123;

    private TextView textview;
    private TelephonyManager mgr;
    private PhoneStateListener psl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        textview = findViewById(R.id.textview);
        textview.setText("");
        psl = new PhoneStateListener() {

            @Override
            public void onCallStateChanged(int state,
                                           String incomingNumber) {
                textview.append(("Status: " + state + "\n") +
                        "Eingehende Rufnummer: " + incomingNumber + "\n");
            }

            @Override
            public void onMessageWaitingIndicatorChanged(boolean mwi) {
                textview.append("onMessageWaitingIndicatorChanged(): " +
                        Boolean.toString(mwi) + "\n");
            }
        };
        try {
            textview.append("SKIP_FIRST_USE_HINTS: " +
                    Settings.Secure.getInt(getContentResolver(),
                            Settings.Secure.SKIP_FIRST_USE_HINTS) +
                    "\n");
        } catch (Settings.SettingNotFoundException e) {
            Log.e(TAG, null, e);
        }
        mgr = getSystemService(TelephonyManager.class);
        if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    RQ_READ_PHONE_STATE);
        } else {
            listen();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        if ((requestCode == RQ_READ_PHONE_STATE) &&
                (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED)) {
            listen();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mgr.listen(psl, PhoneStateListener.LISTEN_NONE);
    }

    private void listen() {
        mgr.listen(psl, PhoneStateListener.LISTEN_CALL_STATE |
                PhoneStateListener.LISTEN_MESSAGE_WAITING_INDICATOR);
    }
}