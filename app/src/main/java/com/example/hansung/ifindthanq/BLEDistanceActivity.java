package com.example.hansung.ifindthanq;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hansung.ifindthanq.Main.MainActivity;
import com.example.hansung.ifindthanq.util.ProblemConfigurationVo;
import com.example.hansung.ifindthanq.util.SQLiteDBHelperDao;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BLEDistanceActivity extends AppCompatActivity {

    private Button connectButton, removeButton;
    private String bleName;
    private String bleMac;
    private int bleImage;
    private String albumImage;
    private SQLiteDBHelperDao mSQLiteDBHelperDao = null;  //객체선언
    private ProblemConfigurationVo problemConfigurationVo;

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice mBtDevice;
    private BluetoothSocket mBtSocket;
    private InputStream mInput;
    private OutputStream mOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bledistance);
        mSQLiteDBHelperDao = new SQLiteDBHelperDao(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = new Intent(this.getIntent());

        bleName = intent.getStringExtra("bleName");
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(bleName);

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
    }

    public void connectBluetooth() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice mBtDevice = mBluetoothAdapter.getRemoteDevice(bleMac);

        try {
            // 연결에 사용할 프로파일을 지정하여 BluetoothSocket 인스턴스를 얻는다
            // 이 예에서는 SPP의 UUID를 사용한다
            mBtSocket = mBtDevice.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            public void run() {
                try {
                    // 소켓을 연결한다
                    mBtSocket.connect();
                    // 입출력을 위한 스트림 오브젝트를 얻는다
                    mInput = mBtSocket.getInputStream();
                    mOutput = mBtSocket.getOutputStream();

                    while (true) {
                        // 입력 데이터를 그대로 출력한다
                        mOutput.write(mInput.read());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            mBtSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
