package com.example.hansung.ifindthanq.Loading;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.hansung.ifindthanq.Main.MainActivity;
import com.example.hansung.ifindthanq.R;

public class LodingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*타이틀 바 제거*/
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();

        setContentView(R.layout.activity_loading);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(2000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }

                Intent intent=new Intent(getApplication(),MainActivity.class);
                startActivity(intent);

            }
        }).start();
    }
}
