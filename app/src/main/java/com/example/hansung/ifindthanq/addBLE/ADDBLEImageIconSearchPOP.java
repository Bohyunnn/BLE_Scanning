package com.example.hansung.ifindthanq.addBLE;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hansung.ifindthanq.R;

public class ADDBLEImageIconSearchPOP extends Activity implements View.OnClickListener {

    private ImageView imageView;
    private ImageButton imageButton1, imageButton2, imageButton3, imageButton4;
    private String resultIcon;
    private String macs, name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀바 제거
        setContentView(R.layout.activity_addbleimage_icon_search_pop);

        imageView = (ImageView) findViewById(R.id.imageView);
        imageButton1 = (ImageButton) findViewById(R.id.imageButton1);
        imageButton2 = (ImageButton) findViewById(R.id.imageButton2);
        imageButton3 = (ImageButton) findViewById(R.id.imageButton3);
        imageButton4 = (ImageButton) findViewById(R.id.imageButton4);


        //이미지 변경
        imageButton1.setOnClickListener(this);
        imageButton2.setOnClickListener(this);
        imageButton3.setOnClickListener(this);
        imageButton4.setOnClickListener(this);

        Intent intent = getIntent();
        if (intent != null) {
            macs = intent.getStringExtra("macs");
            name = intent.getStringExtra("name");
        }

    }

    //이미지 변경
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imageButton1) {
            imageView.setImageResource(R.drawable.dogicon);
            resultIcon = "1";
        } else if (view.getId() == R.id.imageButton2) {
            imageView.setImageResource(R.drawable.headphoneicon);
            resultIcon = "2";
        } else if (view.getId() == R.id.imageButton3) {
            imageView.setImageResource(R.drawable.bluetoothicon);
            resultIcon = "3";
        } else if (view.getId() == R.id.imageButton4) {
            imageView.setImageResource(R.drawable.passport);
            resultIcon = "4";
        }
    }

    //아이콘 이미지 넘겨줌 (->ADDBLEActivity)
    public void onRegisterIcon(View v) {
        Intent intent = new Intent(this, AddBLEActivity.class);
        intent.putExtra("resultIcon", resultIcon);
        intent.putExtra("macs", "" + macs);
        intent.putExtra("name", "" + name);
        startActivity(intent);
    }

    //닫기 버튼
    public void onCancel(View v) {
        //액티비티(팝업) 닫기
        finish();
        Toast.makeText(this, "닫기 누름", Toast.LENGTH_SHORT).show();
    }

    //바깥레이어 클릭시 안닫히게
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }

//    //안드로이드 백버튼 막기
//    @Override
//    public void onBackPressed() {
//        return;
//    }

}
