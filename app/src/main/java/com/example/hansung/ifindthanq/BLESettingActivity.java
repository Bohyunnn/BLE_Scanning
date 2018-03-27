package com.example.hansung.ifindthanq;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hansung.ifindthanq.model.BLE_Setting;

//사용자 BLE [검색주기, 거리] 설정
public class BLESettingActivity extends AppCompatActivity {
    BLE_Setting ble_setting = new BLE_Setting();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blesetting);

//        final String[] scanPeriod = {"10분", "20분", "30분", "1시간", "3시간", "5시간", "10시간"};
//        final String[] signalStrength = {"10m", "20m", "50m", "100m", "100m초과"};

        final Spinner s = (Spinner) findViewById(R.id.spinner);
        final Spinner s2 = (Spinner) findViewById(R.id.spinner2);

//        final ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, scanPeriod);
//
//        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
//        s.setAdapter(adapter);
//
//
//        ArrayAdapter adapter2 = new ArrayAdapter(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, signalStrength);
//
//        adapter2.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
//        s2.setAdapter(adapter2);

        final TextView test1 = (TextView) findViewById(R.id.test1);
        final TextView test2 = (TextView) findViewById(R.id.test2);

        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                test1.setText("주기=>  " + s.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                test2.setText("시간=>  " + s2.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //주기랑 시간=> 알맞은 값으로 변환해야함!!!
        Button SettingButton = (Button) findViewById(R.id.SettingButton);
        final Intent intent = new Intent(this, MainActivity.class);

        SettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ble_setting.setScanPeriod(5000); //주기 변경
                ble_setting.setSignalStrength(-75);  //신호 강도 변경
                Toast.makeText(getApplicationContext(), "[사용자 설정] " +
                        "[주기=" + ble_setting.getScanPeriod() +
                        ", 강도= " + ble_setting.getSignalStrength() + " ]", Toast.LENGTH_SHORT).show();


                startActivity(intent);

            }
        });


    }
}
