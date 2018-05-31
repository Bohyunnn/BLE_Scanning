package com.example.hansung.ifindthanq;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hansung.ifindthanq.Main.MainActivity;
import com.example.hansung.ifindthanq.util.ProblemConfigurationVo;
import com.example.hansung.ifindthanq.util.SQLiteDBHelperDao;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;

public class BLEDistanceActivity extends AppCompatActivity {

    private Button connectButton, noConnectButton, removeButton;

    private String bleName;
    private String bleMac;
    private int bleImage;
    private String albumImage;
    private SQLiteDBHelperDao mSQLiteDBHelperDao = null;  //객체선언
    private ProblemConfigurationVo problemConfigurationVo;

    static final int REQUEST_ENABLE_BT = 10;

    private BluetoothAdapter btAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bledistance);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mSQLiteDBHelperDao = new SQLiteDBHelperDao(this);

        Intent intent = new Intent(this.getIntent());

        bleName = intent.getStringExtra("bleName");
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(bleName);


        actionBar.setTitle("나의 " + bleName + " 블루투스 ");

        bleMac = intent.getStringExtra("bleMac");
        TextView textView1 = (TextView) findViewById(R.id.textView2);
        textView1.setText(bleMac);

        bleImage = intent.getIntExtra("bleImage", 1);
        albumImage = intent.getStringExtra("albumImage");

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        if (albumImage != null) {
            imageView.setImageBitmap(StringToBitMap(albumImage));
        } else {
            imageView.setImageResource(bleImage);
        }

        connectButton = (Button) findViewById(R.id.connectButton);
        removeButton = (Button) findViewById(R.id.removeButton);
        noConnectButton = (Button) findViewById(R.id.noConnectButton);

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSQLiteDBHelperDao.deleteConfiguration(bleName);
                Intent intent1 = new Intent(BLEDistanceActivity.this, MainActivity.class);
                startActivity(intent1);
                finish();
            }
        });

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectBluetooth();
            }
        });

        noConnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disConnectBluetooth();
            }
        });
    }

    //연결 하기
    public void connectBluetooth() {
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        checkBTState(); //블루투스 비활성화시=> 활성화 시킴

        Toast.makeText(getApplicationContext(), bleMac + " 연결을 시도합니다.", Toast.LENGTH_SHORT).show();


        Intent intent = new Intent(getApplicationContext(), BluetoothConnectService.class);
        intent.putExtra("blaMac", bleMac);
        System.out.println("BLEDistanceActivity Service Start!!!>>>>>>>>>>>> " + bleMac);
        startService(intent);
    }

    //연결 해제
    public void disConnectBluetooth() {

        Toast.makeText(getApplicationContext(), bleMac + "연결을 해제합니다.", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getApplicationContext(), BluetoothConnectService.class);
        System.out.println("BLEDistanceActivity Service END!!!>>>>>>>>>>>> " + bleMac);
        stopService(intent);
    }


    //블루투스 활성화 부분1
    private void checkBTState() {
        if (btAdapter == null) {
            errorExit("Fatal Error", "Bluetooth not support");
        } else {
            if (btAdapter.isEnabled()) {
                Log.d("BLEDistanceAcitivy>>>", "...Bluetooth ON...");
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    private void errorExit(String title, String message) {
        Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_LONG).show();
        finish();
    }

    //블루투스 활성화 부분2
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == RESULT_OK) { // 블루투스 활성화 상태
//                    selectDevice();
                } else if (resultCode == RESULT_CANCELED) { // 블루투스 비활성화 상태 (종료)
                    Toast.makeText(getApplicationContext(), "블루투수를 사용할 수 없어 프로그램을 종료합니다",
                            Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    //---------------------------------------------------

    public Bitmap StringToBitMap(String image) {
        try {
            byte[] encodeByte = Base64.decode(image, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
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