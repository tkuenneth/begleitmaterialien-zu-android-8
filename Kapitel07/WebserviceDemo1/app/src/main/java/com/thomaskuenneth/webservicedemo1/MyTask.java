package com.thomaskuenneth.webservicedemo1;


import android.os.AsyncTask;
import android.widget.TextView;

import java.lang.ref.WeakReference;

class MyTask extends AsyncTask<String, Void, String> {

    // vermeidet this fields leaks a context...
    private final WeakReference<TextView> r;

    MyTask(TextView a) {
        r = new WeakReference<>(a);
    }

    @Override
    protected String doInBackground(String... params) {
        return WebserviceDemo1Activity.shortenURL(params[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        r.get().setText(result);
    }
}
