package com.thomaskuenneth.blescannerdemo;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BLEScannerActivity extends Activity {

    private static final int REQUEST_ACCESS_COARSE_LOCATION
            = 321;

    private final ScanCallback scanCallback = new ScanCallback() {

        @Override
        public void onScanFailed(int errorCode) {
            Toast.makeText(BLEScannerActivity.this,
                    getString(R.string.error, errorCode), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            updateData(result);
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            for (ScanResult result : results) {
                updateData(result);
            }
        }
    };

    private ArrayAdapter<String> listAdapter;
    private Map<String, ScanResult> scanResults;
    private TextView tv;
    private BluetoothAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        listAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1);
        scanResults = new HashMap<>();
        final ListView lv = findViewById(R.id.lv);
        lv.setAdapter(listAdapter);
        lv.setOnItemClickListener((adapterView, view, pos, id) -> {
            String address = listAdapter.getItem(pos);
            ScanResult result = scanResults.get(address);
            info(result);
        });
        tv = findViewById(R.id.tv);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter = null;
        listAdapter.clear();
        scanResults.clear();
        tv.setText("");
        if (checkSelfPermission(
                Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]
                            {Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_ACCESS_COARSE_LOCATION);
        } else {
            startOrFinish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (adapter != null) {
            scan(false);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        if ((requestCode == REQUEST_ACCESS_COARSE_LOCATION) &&
                (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            startOrFinish();
        } else {
            finish();
        }
    }

    private void startOrFinish() {
        if (isBluetoothEnabled()) {
            scan(true);
        } else {
            finish();
        }
    }

    private boolean isBluetoothEnabled() {
        boolean enabled = false;
        final BluetoothManager m = getSystemService(BluetoothManager.class);
        if (m != null) {
            adapter = m.getAdapter();
            if (adapter != null) {
                enabled = adapter.isEnabled();
                if (!enabled) {
                    Toast.makeText(this, R.string.enable_bluetooth, Toast.LENGTH_LONG)
                            .show();
                }
            }
        }
        return enabled;
    }

    private void scan(boolean enable) {
        BluetoothLeScanner scanner = adapter.getBluetoothLeScanner();
        if (enable) {
            scanner.startScan(scanCallback);
        } else {
            scanner.stopScan(scanCallback);
        }
    }

    private void updateData(ScanResult result) {
        String address = result.getDevice().getAddress();
        if (!scanResults.containsKey(address)) {
            listAdapter.add(address);
            listAdapter.notifyDataSetChanged();
        }
        scanResults.put(address, result);
    }

    private void info(ScanResult result) {
        tv.setText(result.toString());
    }
}
