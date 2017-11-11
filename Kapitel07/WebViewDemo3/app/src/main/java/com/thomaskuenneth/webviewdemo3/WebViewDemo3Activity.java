package com.thomaskuenneth.webviewdemo3;

import android.app.Activity;
import android.os.Bundle;
// import android.webkit.WebResourceRequest;
import android.webkit.WebView;
// import android.webkit.WebViewClient;

public class WebViewDemo3Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final WebView wv = findViewById(R.id.webview);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.loadUrl("file:///android_asset/test1.html");
//        WebViewClient client = new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view,
//                                                    WebResourceRequest request) {
//                return false;
//            }
//        };
//        wv.setWebViewClient(client);
//        wv.loadUrl("file:///android_asset/test2.html");
        wv.addJavascriptInterface(new WebAppInterface(this), "Android");
    }
}
