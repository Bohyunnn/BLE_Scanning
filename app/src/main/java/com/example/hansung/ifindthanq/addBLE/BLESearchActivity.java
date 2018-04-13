package com.example.hansung.ifindthanq.addBLE;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.hansung.ifindthanq.R;
import com.example.hansung.ifindthanq.model.SearchBLE;

import java.util.ArrayList;

public class BLESearchActivity extends AppCompatActivity {

    BluetoothAdapter mBluetoothAdapter;
    private CustomAdapter adp;
    private Thread t = null;
    private ArrayList<SearchBLE> SearchBLE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blesearch);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter.startDiscovery();

        SearchBLE = new ArrayList<>();

        adp = new CustomAdapter(SearchBLE);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adp);
        recyclerView.setLayoutManager(new LinearLayoutManager(BLESearchActivity.this));
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter.startDiscovery();

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                adp.setContext(context);
                String deviceName;
                if(device.getName()==null){
                    deviceName="Unknown Device";
                } else{
                    deviceName=device.getName();
                }

                SearchBLE.add(new SearchBLE(device.getAddress(), deviceName));

                RecyclerView list = (RecyclerView) findViewById(R.id.recyclerView);
                list.setAdapter(adp);
            }
        }
    };

    public void stopUpdateThread(View v) {
        if (t != null)
            t.interrupt();
    }

}