package com.example.hansung.ifindthanq;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hansung.ifindthanq.Main.MainActivity;
import com.example.hansung.ifindthanq.util.ProblemConfigurationVo;
import com.example.hansung.ifindthanq.util.SQLiteDBHelperDao;

public class BLEDistanceActivity extends AppCompatActivity {

    private Button connectButton, removeButton;
    String bleName;
    String bleMac;
    private int bleImage;
    private SQLiteDBHelperDao mSQLiteDBHelperDao = null;  //객체선언
    private ProblemConfigurationVo problemConfigurationVo;


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
        ImageView imageView = (ImageView) findViewById(R.id.imageView);

        imageView.setImageResource(bleImage);

        connectButton = (Button) findViewById(R.id.connectButton);
        removeButton = (Button) findViewById(R.id.removeButton);

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSQLiteDBHelperDao.deleteConfiguration(bleMac);

                Intent intent1 = new Intent(BLEDistanceActivity.this, MainActivity.class);
                startActivity(intent1);
                finish();
            }
        });
    }

    //연결 기능
    public void onConnect(View view) {
// 해당 Mac 값 연결

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
