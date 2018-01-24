package com.thomaskuenneth.widgetdemo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

public class WidgetDemo extends Activity {

    private static final String TAG = WidgetDemo.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        // Referenzen auf Views ermitteln
        final FrameLayout f = findViewById(R.id.frame);
        final EditText e = findViewById(R.id.textfield);
        final Button b = findViewById(R.id.apply);
        // auf Anklicken des Buttons reagieren
        b.setOnClickListener(v -> {
            // eingegebenen Text auslesen
            String name = e.getText().toString();
            try {
                // ein Objekt instanziieren
                Class<?> c = Class.forName(name);
                Object o = c.getDeclaredConstructor(Context.class)
                        .newInstance(WidgetDemo.this);
                // Leitet die Klasse von View ab?
                if (o instanceof View) {
                    // alte Views löschen u. neue hinzufügen
                    f.removeAllViews();
                    f.addView((View) o, params);
                    f.forceLayout();
                }
            } catch (Throwable tr) {
                String str = getString(R.string.error, name);
                Toast.makeText(WidgetDemo.this, str, Toast.LENGTH_LONG).show();
                Log.e(TAG, "Fehler beim Instanziieren von " + name, tr);
            }
        });
        // auf "Go" reagieren
        e.setOnEditorActionListener((v, actionId, event) -> {
            b.performClick();
            return true;
        });
    }
}