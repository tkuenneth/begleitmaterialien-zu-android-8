package com.thomaskuenneth.accountdemo1;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import static android.accounts.AccountManager.KEY_ACCOUNT_NAME;
import static android.accounts.AccountManager.KEY_ACCOUNT_TYPE;

public class AccountDemo1 extends Activity {

    private static final int RQ_ACCOUNT_INTENT = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = AccountManager.newChooseAccountIntent(null,
                null,
                null,
                null,
                null,
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
                String accountName = data.getStringExtra(KEY_ACCOUNT_NAME);
                String accountType = data.getStringExtra(KEY_ACCOUNT_TYPE);
                Toast.makeText(this, getString(R.string.template, accountName, accountType),
                        Toast.LENGTH_LONG).show();
            }
        }
        finish();
    }
}