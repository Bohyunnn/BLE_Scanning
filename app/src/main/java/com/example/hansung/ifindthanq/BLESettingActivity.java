package com.example.hansung.ifindthanq;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hansung.ifindthanq.Main.MainActivity;
import com.example.hansung.ifindthanq.model.BLE_Setting;

import java.util.ArrayList;
import java.util.List;

//사용자 BLE [검색주기, 거리] 설정
public class BLESettingActivity extends AppCompatActivity {
    private Button settingButton;
    private TextView meterText;
    private Spinner spinner;
    private String meter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blesetting);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("블루투스 거리 설정");

        meterText = (TextView) findViewById(R.id.meterText);
        settingButton = (Button) findViewById(R.id.settingButton);

        //스피너 설정
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                meter = adapterView.getItemAtPosition(i).toString();
                if (meter != "")
                    meterText.setText(meter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), meter + "로 설정합니다.", Toast.LENGTH_SHORT).show();
                //sqlite(update)=[meter] 변수 저장

            }
        });


        //데이터를 저장하게 되는 리스트
        List<String> spinner_items = new ArrayList<>();

        //스피너와 리스트를 연결하기 위해 사용되는 어댑터
        ArrayAdapter<String> spinner_adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, spinner_items);

        spinner_items.add("2M이상");
        spinner_items.add("5M이상");
        spinner_items.add("10M이상");

        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //스피너의 어댑터 지정
        spinner.setAdapter(spinner_adapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
