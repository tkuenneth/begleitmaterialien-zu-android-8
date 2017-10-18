package com.thomaskuenneth.openweathermapweather;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private EditText city;
    private ImageView image;
    private TextView temperatur, beschreibung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!istNetzwerkVerfuegbar()) {
            finish();
        }
        setContentView(R.layout.activity_main);
        city = findViewById(R.id.city);
        image = findViewById(R.id.image);
        temperatur = findViewById(R.id.temperatur);
        beschreibung = findViewById(R.id.beschreibung);
        final Button button = findViewById(R.id.button);
        button.setOnClickListener((v) -> new Thread(() -> {
            try {
                final WeatherData weather = WeatherUtils
                        .getWeather(city.getText().toString());
                final Bitmap bitmapWeather =
                        WeatherUtils.getImage(weather);
                runOnUiThread(() -> {
                    city.setText(weather.name);
                    image.setImageBitmap(bitmapWeather);
                    beschreibung.setText(weather.description);
                    Double temp = weather.temp - 273.15;
                    temperatur.setText(getString(R.string.temp_template,
                            temp.intValue()));
                });
            } catch (Exception e) {
                Log.e(TAG, "getWeather()", e);
            }
        }).start());
        city.setOnEditorActionListener(
                (textView, i, keyEvent) -> {
                    button.performClick();
                    return true;
                });
    }

    private boolean istNetzwerkVerfuegbar() {
        ConnectivityManager mgr =
                getSystemService(ConnectivityManager.class);
        NetworkInfo info = mgr == null ? null : mgr.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }
}
