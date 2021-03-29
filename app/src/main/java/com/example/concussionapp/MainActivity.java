package com.example.concussionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, Toolbar.OnMenuItemClickListener {
    private LeDeviceListAdapter mLeDeviceListAdapter;
    private BluetoothAdapter mBluetoothAdapter;

    private ScanSettings scanSettings;

    BluetoothLeScanner mBluetoothScanner;
    TextView mDeviceView;

    private boolean mScanning;
    private Handler mHandler;

    BottomNavigationView bottomNavigationView;
    MaterialToolbar materialToolbar;

    private static final int REQUEST_ENABLE_BT = 1;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);

        materialToolbar = findViewById(R.id.topAppBar);
        materialToolbar.setOnMenuItemClickListener(this);

        mHandler = new Handler();

        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "BLE is not supported", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        mBluetoothScanner = mBluetoothAdapter.getBluetoothLeScanner();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth not supported", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        mLeDeviceListAdapter = new LeDeviceListAdapter();

        scanSettings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
                .build();
    }

    Home homeFragment = new Home();
    Calendar calFragment = new Calendar();
    Newsfeed newsFragment = new Newsfeed();
    Faq faqFragment = new Faq();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        materialToolbar = findViewById(R.id.topAppBar);
        switch (item.getItemId()) {
            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                materialToolbar.setTitle(" Home");
                materialToolbar.setLogo(R.drawable.ic_baseline_home_24);
                return true;
            case R.id.calendar:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, calFragment).commit();
                materialToolbar.setTitle(" Calendar");
                materialToolbar.setLogo(R.drawable.ic_baseline_event_24);
                return true;
            case R.id.newsfeed:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, newsFragment).commit();
                materialToolbar.setTitle(" Newsfeed");
                materialToolbar.setLogo(R.drawable.ic_baseline_text_snippet_24);
                return true;
            case R.id.faq:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, faqFragment).commit();
                materialToolbar.setTitle(" FAQ");
                materialToolbar.setLogo(R.drawable.ic_baseline_not_listed_location_24);
                return true;
        }
        return false;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        String[] items = {"Device 1", "Device 2", "Device 3"}; // Replace with array of the bluetooth devices

        if (item.getItemId() == R.id.bluetooth) {
            scanLeDevice(true);
//            new AlertDialog.Builder(this).setTitle("Select Bluetooth Device").setItems(items, ((dialog, which) -> {
//                Log.i("a", "b");
//                // Replace above line with a function that takes in which (an integer designating which
//                // item was clicked) as a parameter and then connect to the corresponding bluetooth
//                // device.
//            })).show();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //inflate layout from xm
            builder.setAdapter(mLeDeviceListAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final BluetoothDevice device = mLeDeviceListAdapter.getDevice(which);
                    Log.i("connected:", device.getName());
                    if (device == null) return;
                    // TODO: connect to Bluetooth here and start graph activity
//                    final Intent intent = new Intent(this, DeviceControlActivity.class);
//                    intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_NAME, device.getName());
//                    intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_ADDRESS, device.getAddress());
                    if (mScanning) {
                        mBluetoothScanner.stopScan(mLeScanCallback);
                        mScanning = false;
                    }
                }
            });
            builder.show();
            return true;
        }
        return false;
    }

    private void scanLeDevice(final boolean enable) { ;
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothScanner.stopScan((ScanCallback) mLeScanCallback);
                    invalidateOptionsMenu();
                }
            }, SCAN_PERIOD);

            // Filter for only HM-10 Modules
            mScanning = true;
            UUID BLE_MODULE = UUID.fromString("0000FFE0-0000-1000-8000-00805F9B34FB");
            UUID[] uuid = new UUID[]{BLE_MODULE};
            List<ScanFilter> filters = null;
            if (uuid != null) {
                filters = new ArrayList<>();
                for (UUID serviceUUID : uuid) {
                    ScanFilter filter = new ScanFilter.Builder()
                            .setServiceUuid(new ParcelUuid(serviceUUID))
                            .build();
                    filters.add(filter);
                }
            }
            mBluetoothScanner.startScan(filters, scanSettings, mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothScanner.stopScan(mLeScanCallback);
        }
        invalidateOptionsMenu();
    }

    // Device scan callback
    private ScanCallback mLeScanCallback =
            new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    super.onScanResult(callbackType, result);
                    mLeDeviceListAdapter.addDevice(result.getDevice());
                    mLeDeviceListAdapter.notifyDataSetChanged();
//                    Log.i("result:", result.toString()); // check if scan is working
//                    BluetoothDevice btDevice = result.getDevice();
                }
            };


    private class LeDeviceListAdapter extends BaseAdapter {
        private ArrayList<BluetoothDevice> mLeDevices;
        private LayoutInflater mInflator;

        public LeDeviceListAdapter() {
            super();
            mLeDevices = new ArrayList<BluetoothDevice>();
            mInflator = MainActivity.this.getLayoutInflater();
        }

        public void addDevice(BluetoothDevice device) {
            if(!mLeDevices.contains(device))
            {
                mLeDevices.add(device);
            }
        }

        public BluetoothDevice getDevice(int position) {
            return mLeDevices.get(position);
        }

        public void clear() {
            mLeDevices.clear();
        }

        @Override
        public int getCount() {
            return mLeDevices.size();
        }

        @Override
        public Object getItem(int i) {
            return mLeDevices.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            // General ListView optimization code.
            if (view == null) {
                view = mInflator.inflate(R.layout.listitem_device, null);
                viewHolder = new ViewHolder();
                viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            BluetoothDevice device = mLeDevices.get(i);
            final String deviceName = device.getName();
            if (deviceName != null && deviceName.length() > 0) {
                viewHolder.deviceName.setText(deviceName);
            }
            else
                viewHolder.deviceName.setText("Unknown Device");
            return view;
        }
    }

    static class ViewHolder {
        TextView deviceName;
    }
}