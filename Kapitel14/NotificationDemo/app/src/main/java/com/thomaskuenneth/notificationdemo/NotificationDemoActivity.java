package com.thomaskuenneth.notificationdemo;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class NotificationDemoActivity extends Activity {

    private static final int NOTIFICATION_ID = 42;
    private static final String EXTRA_VOICE_REPLY = "sprachantwort";
    private static final String CHANNEL_ID = "myChannel";

    private EditText edittext;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final CheckBox local = findViewById(R.id.local);
        final CheckBox ongoing = findViewById(R.id.ongoing);
        final CheckBox page = findViewById(R.id.page);
        edittext = findViewById(R.id.edittext);
        edittext.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s,
                                          int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(CharSequence s,
                                      int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateButton();
            }
        });
        button = findViewById(R.id.button);
        button.setOnClickListener(v -> createAndSendNotification(
                edittext.getText().toString(),
                ongoing.isChecked(),
                local.isChecked(),
                page.isChecked()));
        // Kanal erzeugen
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                getString(R.string.channel_name),
                NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager nm = getSystemService(NotificationManager.class);
        if (nm != null) {
            nm.createNotificationChannel(channel);
        }
        // Wurde ein Intent empfangen? Dann verarbeiten
        Intent intent = getIntent();
        if (intent != null) {
            CharSequence text = getMessageText(intent);
            if (text != null) {
                edittext.setText(text);
            }
        }
        // Status des Buttons aktualisieren
        updateButton();
    }

    private void createAndSendNotification(String txt,
                                           boolean ongoing,
                                           boolean local,
                                           boolean secondPage) {
        // wird für Reaktion auf die Nachricht benötigt
        PendingIntent reply = PendingIntent.getActivity(this, 0,
                new Intent(this, NotificationDemoActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
        // Builder für die Nachricht
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(txt)
                        .setOngoing(ongoing)
                        .setLocalOnly(local)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentIntent(reply);
        // mit dem Extender Wearable-Spezialitäten hinzufügen
        NotificationCompat.WearableExtender extender =
                new NotificationCompat.WearableExtender();
        // Der Nachricht eine zweite Seite hinzufügen?
        if (secondPage) {
            extender.addPage(createSecondPage(txt));
        }
        // Aktion für Sprachantwort hinzufügen
        extender.addAction(createVoiceReplyAction(reply));
        // Nachricht fertigstellen und anzeigen
        notificationBuilder.extend(extender);
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(
                        NotificationDemoActivity.this);
        notificationManager.notify(NOTIFICATION_ID,
                notificationBuilder.build());
    }

    private Notification createSecondPage(String txt) {
        // einen großen, langen Text erzeugen
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append(txt);
        }
        NotificationCompat.BigTextStyle secondPageStyle =
                new NotificationCompat.BigTextStyle();
        secondPageStyle.setBigContentTitle(getString(R.string.page2))
                .bigText(sb.toString());
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setStyle(secondPageStyle)
                .build();
    }

    private NotificationCompat.Action createVoiceReplyAction(
            PendingIntent pendingIntent) {
        String replyLabel = getString(R.string.reply);
        String[] choices =
                getResources().getStringArray(R.array.choices);
        RemoteInput remoteInput =
                new RemoteInput.Builder(EXTRA_VOICE_REPLY)
                        .setLabel(replyLabel)
                        .setChoices(choices)
                        .build();
        return new NotificationCompat.Action.Builder(
                R.drawable.ic_launcher,
                getString(R.string.app_name), pendingIntent)
                .addRemoteInput(remoteInput)
                .build();
    }

    private CharSequence getMessageText(Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            return remoteInput.getCharSequence(EXTRA_VOICE_REPLY);
        }
        return null;
    }

    private void updateButton() {
        button.setEnabled(edittext.getText().length() > 0);
    }
}