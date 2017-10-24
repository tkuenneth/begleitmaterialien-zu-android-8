package com.thomaskuenneth.leeresprojekt;

import android.app.Activity;
import android.os.Bundle;

/**
 * Hinweis: die Layouts framelayout_demo, linearlayout_demo und relativelayout_demo
 * werden in Kapitel 5 verwendet
 */
public class MainActivity extends Activity {

//    private static final int RQ_INSERT_CONTACT = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ab hier beliebigen Code einfügen...

        // Einen neuen Kontaktanlegen
        // Nach dem Entfernen der Kommentare fehlende Importe ergänzen
//        Intent in = new Intent(Intent.ACTION_INSERT,
//                ContactsContract.Contacts.CONTENT_URI);
//        in.putExtra("finishActivityOnSaveCompleted", true);
//        in.putExtra(ContactsContract.Intents.Insert.NAME,
//                "Max Mustermann");
//        in.putExtra(ContactsContract.Intents.Insert.PHONE,
//                "+49 (123) 45 67 89");
//        in.putExtra(ContactsContract.Intents.Insert.PHONE_TYPE,
//                ContactsContract.CommonDataKinds.Phone.TYPE_WORK);

        // Mehrere Datensätze übergeben
//        ArrayList<ContentValues> data = new ArrayList<>();
//        ContentValues v1 = new ContentValues();
//        v1.put(ContactsContract.Contacts.Data.MIMETYPE,
//                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
//        v1.put(ContactsContract.CommonDataKinds.Phone.NUMBER, "123");
//        v1.put(ContactsContract.CommonDataKinds.Phone.TYPE,
//                ContactsContract.CommonDataKinds.Phone.TYPE_HOME);
//        data.add(v1);
//        ContentValues v2 = new ContentValues();
//        v2.put(ContactsContract.Contacts.Data.MIMETYPE,
//                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
//        v2.put(ContactsContract.CommonDataKinds.Phone.NUMBER, "456");
//        v2.put(ContactsContract.CommonDataKinds.Phone.TYPE,
//                ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
//        in.putParcelableArrayListExtra(
//                ContactsContract.Intents.Insert.DATA, data);
//        data.add(v2);
//        in.putExtra("finishActivityOnSaveCompleted", true);

//        startActivity(in);
//        startActivityForResult(in, RQ_INSERT_CONTACT);
    }

//    @Override
//    protected void onActivityResult(int requestCode,
//                                    int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == RQ_INSERT_CONTACT) {
//            if (resultCode == RESULT_OK) {
//                if (data != null) {
//                    Intent i =
//                            new Intent(Intent.ACTION_VIEW,
//                                    data.getData());
//                    startActivity(i);
//                }
//            }
//        }
//    }
}
