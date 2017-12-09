package com.thomaskuenneth.accountdemo2;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AccountDemo2 extends Activity implements
        AccountManagerCallback<Bundle> {

    private static final int RQ_ACCOUNT_INTENT = 123;
    private static final String TYPE = "com.google";
    private static final String AUTH_TOKEN_TYPE = "cl";

    // Schlüssel unter https://console.developers.google.com/ anlegen
    private static final String
            API_KEY = "<Ihr API-Schlüssel>";

    private static final String TAG =
            AccountDemo2.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = AccountManager.newChooseAccountIntent(null,
                null,
                new String[]{TYPE},
                null,
                AUTH_TOKEN_TYPE,
                null,
                null);
        startActivityForResult(i, RQ_ACCOUNT_INTENT);
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((RESULT_OK == resultCode)
                && (RQ_ACCOUNT_INTENT == requestCode)) {
            if (data != null) {
                accessAccount();
            }
        }
    }

    private void accessAccount() {
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.LAX);
        AccountManager accountManager = AccountManager.get(this);
        try {
            Account[] accounts =
                    accountManager.getAccountsByType(TYPE);
            if (accounts.length == 1) {
                Bundle options = new Bundle();
                accountManager.getAuthToken(accounts[0],
                        AUTH_TOKEN_TYPE, options,
                        this, this, null);
            } else {
                finish();
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getAccountsByType()", e);
        }

    }

    @Override
    public void run(AccountManagerFuture<Bundle> future) {
        Bundle result;
        try {
            result = future.getResult();
            String token =
                    result.getString(AccountManager.KEY_AUTHTOKEN);
            String tasks = getFromServer("https://www.googleapis.com/"
                    + "tasks/v1/lists/@default/tasks?pp=1&key="
                    + API_KEY, token);
            Log.d(TAG, tasks);
        } catch (OperationCanceledException |
                AuthenticatorException |
                IOException e) {
            Log.e(TAG, "run()", e);
        }
    }

    private String getFromServer(String _url,
                                 String token) {
        StringBuilder sb = new StringBuilder();
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(_url);
            httpURLConnection =
                    (HttpURLConnection) url.openConnection();
            httpURLConnection.
                    setRequestProperty("Authorization",
                            "GoogleLogin auth=" + token);
            int responseCode =
                    httpURLConnection.getResponseCode();
            if (responseCode ==
                    HttpURLConnection.HTTP_OK) {
                InputStreamReader inputStreamReader =
                        new InputStreamReader(
                                httpURLConnection.getInputStream());
                BufferedReader bufferedReader =
                        new BufferedReader(
                                inputStreamReader);
                int i;
                while ((i = bufferedReader.read()) != -1) {
                    sb.append((char) i);
                }
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    Log.e(TAG, "close()", e);
                }
            } else {
                Log.d(TAG, "responseCode: " + responseCode);
            }
        } catch (IOException tr) {
            Log.e(TAG, "Fehler beim Zugriff auf " + _url, tr);
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return sb.toString();
    }
}
