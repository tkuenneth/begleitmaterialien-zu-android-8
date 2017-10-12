package com.thomaskuenneth.listenerdemo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class ListenerDemo extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final TextView textview = findViewById(R.id.textview);
        final CheckBox checkbox = findViewById(R.id.checkbox);
        // OnClickListener für CheckBox registrieren
        checkbox.setOnClickListener(v
                -> textview.setText(Boolean.toString(checkbox.isChecked())));
        final Button status = findViewById(R.id.status);
        // OnClickListener für Button registrieren
        status.setOnClickListener(v
                -> checkbox.setChecked(!checkbox.isChecked()));

        checkbox.setOnCheckedChangeListener((buttonView, isChecked)
                -> textview.setText(Boolean.toString(checkbox.isChecked())));
    }
}
