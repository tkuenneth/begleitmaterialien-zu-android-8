package com.thomaskuenneth.appshortcutdemo;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.widget.Toast;

import java.util.Collections;

public class AppShortcutActivity extends Activity {

    private static final String ACTION =
            "com.thomaskuenneth.appshortcutdemo.AppShortcut";

    @Override
    protected void onStart() {
        super.onStart();
        Intent i = getIntent();
        if ((i != null) &&
                (ACTION.equals(i.getAction()))) {
            Uri uri = i.getData();
            String s = (uri == null)
                    ? getString(R.string.txt_static)
                    : uri.toString();
            Toast.makeText(this, s,
                    Toast.LENGTH_LONG).show();
        }
        // dynamisches Shortcut
        Intent intent = new Intent(this, AppShortcutActivity.class);
        intent.setAction(ACTION);
        intent.setData(Uri.parse("https://www.rheinwerk-verlag.de/"));
        ShortcutManager mgr =
                getSystemService(ShortcutManager.class);
        if (mgr != null) {
            ShortcutInfo shortcut = new ShortcutInfo.Builder(this,
                    "dynamic1")
                    .setShortLabel(getString(R.string.dynamic_shortcut))
                    .setIcon(Icon.createWithResource(this,
                            R.drawable.ic_cloud))
                    .setIntent(intent)
                    .build();
            mgr.setDynamicShortcuts(Collections.singletonList(shortcut));
        }
        finish();
    }
}
