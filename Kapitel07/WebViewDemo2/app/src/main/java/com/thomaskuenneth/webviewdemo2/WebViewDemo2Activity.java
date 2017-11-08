package com.thomaskuenneth.webviewdemo2;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;


public class WebViewDemo2Activity extends Activity {

    private Button prev, next;
    private EditText edittext;
    private WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        // Schaltfläche "Zurück"
        prev = findViewById(R.id.prev);
        prev.setOnClickListener(v -> webview.goBack());
        // Schaltfläche "Weiter"
        next = findViewById(R.id.next);
        next.setOnClickListener(v -> webview.goForward());
        // Eingabezeile
        edittext = findViewById(R.id.edittext);
        edittext.setOnEditorActionListener(
                (v, actionId, event) -> {
                    webview.loadUrl(v.getText().toString());
                    return true;
                });
        // WebView
        webview = findViewById(R.id.webview);
        webview.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view,
                                                    WebResourceRequest r) {
                view.loadUrl(r.getUrl().toString());
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                updateNavBar();
            }

            @Override
            public void onReceivedError(WebView view,
                                        WebResourceRequest request,
                                        WebResourceError error) {
                updateNavBar();
            }
        });
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setUseWideViewPort(true);
        if (savedInstanceState != null) {
            webview.restoreState(savedInstanceState);
        } else {
            webview.loadUrl("https://www.rheinwerk-verlag.de/");
        }
        webview.requestFocus();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        webview.saveState(outState);
    }

    private void updateNavBar() {
        prev.setEnabled(webview.canGoBack());
        next.setEnabled(webview.canGoForward());
        edittext.setText(webview.getUrl());
    }
}
