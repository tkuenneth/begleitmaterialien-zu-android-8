/*
 * This file is part of C64 Tribute Watch Face
 * Copyright (C) 2014 - 2017  Thomas Kuenneth
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.thomaskuenneth.c64watchface;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

/**
 * This class represents the settings activity for the watch face on the wearable device.
 *
 * @author Thomas Kuenneth
 */
public class C64WatchFaceWearableConfigActivity extends Activity {

    private SharedPreferences prefs;
    private CheckBox cbSecondsVisible;
    private CheckBox cbDateVisible;
    private CheckBox cbUpperCase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wearable_config);
        prefs = getSharedPreferences(C64WatchFaceService.PREFS_NAME, MODE_PRIVATE);
        cbSecondsVisible = findViewById(R.id.checkbox_seconds);
        cbSecondsVisible.setChecked(prefs.getBoolean(C64WatchFaceService.PREFS_SECONDS, false));
        cbDateVisible = findViewById(R.id.checkbox_date);
        cbDateVisible.setChecked(prefs.getBoolean(C64WatchFaceService.PREFS_DATE, false));
        cbUpperCase = findViewById(R.id.checkbox_uppercase);
        cbUpperCase.setChecked(prefs.getBoolean(C64WatchFaceService.PREFS_UPPERCASE, false));
        CompoundButton.OnCheckedChangeListener l =
                new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        SharedPreferences.Editor e = prefs.edit();
                        e.putBoolean(C64WatchFaceService.PREFS_SECONDS, cbSecondsVisible.isChecked());
                        e.putBoolean(C64WatchFaceService.PREFS_DATE, cbDateVisible.isChecked());
                        e.putBoolean(C64WatchFaceService.PREFS_UPPERCASE, cbUpperCase.isChecked());
                        e.apply();
                    }
                };
        cbSecondsVisible.setOnCheckedChangeListener(l);
        cbDateVisible.setOnCheckedChangeListener(l);
        cbUpperCase.setOnCheckedChangeListener(l);
    }
}
