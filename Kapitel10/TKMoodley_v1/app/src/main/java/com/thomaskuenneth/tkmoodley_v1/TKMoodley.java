package com.thomaskuenneth.tkmoodley_v1;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class TKMoodley extends Activity {

    private TKMoodleyOpenHandler openHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final ImageButton buttonGut =
                findViewById(R.id.gut);
        buttonGut.setOnClickListener((e) ->
                imageButtonClicked(TKMoodleyOpenHandler.MOOD_FINE)
        );
        final ImageButton buttonOk =
                findViewById(R.id.ok);
        buttonOk.setOnClickListener((e) ->
                imageButtonClicked(TKMoodleyOpenHandler.MOOD_OK)
        );
        final ImageButton buttonSchlecht =
                findViewById(R.id.schlecht);
        buttonSchlecht.setOnClickListener((e) ->
                imageButtonClicked(TKMoodleyOpenHandler.MOOD_BAD)
        );
        final Button buttonAuswertung =
                findViewById(R.id.auswertung);
        buttonAuswertung.setOnClickListener((e) -> {
            // hier passiert noch nichts
        });
        openHandler = new TKMoodleyOpenHandler(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        openHandler.close();
    }

    private void imageButtonClicked(int mood) {
        openHandler.insert(mood, System.currentTimeMillis());
        Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT)
                .show();
    }
}
