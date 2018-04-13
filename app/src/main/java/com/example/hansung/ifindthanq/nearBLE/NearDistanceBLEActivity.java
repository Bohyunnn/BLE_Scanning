package com.example.hansung.ifindthanq.nearBLE;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.hansung.ifindthanq.R;
import com.example.hansung.ifindthanq.model.NearBLE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NearDistanceBLEActivity extends AppCompatActivity {

    BluetoothAdapter mBluetoothAdapter;
    private CustomNearAdapter adp;
    private Thread t = null;
    private ArrayList<NearBLE> nearBLES;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_distance_ble);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter.startDiscovery();

        nearBLES = new ArrayList<>();

        adp = new CustomNearAdapter(nearBLES);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adp);
        recyclerView.setLayoutManager(new LinearLayoutManager(NearDistanceBLEActivity.this));
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
            int dist = 0;
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
                if (rssi > -45) {
                    dist = 0;
                } else if (-45 > rssi && rssi >= -48)
                    dist = 1;
                else if (-48 > rssi && rssi >= -51)
                    dist = 2;
                else if (-51 > rssi && rssi >= -54)
                    dist = 3;
                else if (-54 > rssi && rssi >= -58)
                    dist = 4;
                else if (-58 > rssi && rssi >= -61)
                    dist = 5;
                else if (-61 > rssi && rssi >= -68)
                    dist = 6;
                else if (-68 > rssi && rssi >= -71)
                    dist = 7;
                else if (-71 > rssi && rssi >= -76)
                    dist = 8;
                else if (-76 > rssi && rssi >= -80)
                    dist = 9;
                else if (-80 > rssi)
                    dist = 10;


                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                //rssi 값
                String rssiResult = rssi + "db";
                String distance = dist + "M";

                //현재시간
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String getTime = sdf.format(date);

                //장치 이름이 없는 경우
//                Toast.makeText(getApplicationContext(), "[Device Name]== " + device.getName(), Toast.LENGTH_SHORT).show();
                String deviceName;
                if (device.getName() == null) {
                    deviceName = "UnKnown Device";
                } else {
                    deviceName = device.getName();
                }

                nearBLES.add(new NearBLE(deviceName, device.getAddress(), getTime, rssiResult, distance));

                // System.out.println("[Device]==> Mac: " + device.getAddress() + "/ Name: " + device.getName() + "/UUID: " + device.getUuids());
                // Log.i("BT", device.getName() + "\n" + device.getAddress());

                adp.setContext(context);
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
