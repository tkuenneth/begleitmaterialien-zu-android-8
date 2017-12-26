package com.thomaskuenneth.kalenderdemo1;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

public class KalenderDemo1 extends Activity {

    private static final String TAG =
            KalenderDemo1.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Beginn und Ende eines Termins
        Calendar cal = Calendar.getInstance();
        Date from = cal.getTime();
        cal.add(Calendar.HOUR_OF_DAY, 1);
        Date to = cal.getTime();
        // Termin anlegen
        createEntry(getString(R.string.title),
                getString(R.string.hello), from, to,
                cal.get(Calendar.HOUR_OF_DAY) < 12);
        finish();
    }

    private void createEntry(String title, String description, Date from,
                             Date to, boolean allDay) {
        Intent intent = new Intent(Intent.ACTION_INSERT,
                Events.CONTENT_URI);
        intent.putExtra(Events.TITLE, title);
        intent.putExtra(Events.DESCRIPTION, description);
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, from.getTime());
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, to.getTime());
        intent.putExtra(Events.ALL_DAY, allDay);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Log.e(TAG, "keine passende Activity", e);
        }
    }
}
