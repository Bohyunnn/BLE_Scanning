package com.example.hansung.ifindthanq;

import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class BLEDistanceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bledistance);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = new Intent(this.getIntent());

        String bleName = intent.getStringExtra("bleName");
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(bleName);

        String bleMac = intent.getStringExtra("bleMac");
        TextView textView1 = (TextView) findViewById(R.id.textView2);
        textView1.setText(bleMac);

        int bleImage = intent.getIntExtra("bleImage", 1);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);

        imageView.setImageResource(bleImage);
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
