package com.example.hansung.ifindthanq.addBLE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hansung.ifindthanq.Main.MainActivity;
import com.example.hansung.ifindthanq.Main.MyBLEAdapter;
import com.example.hansung.ifindthanq.R;
import com.example.hansung.ifindthanq.model.MyBLE;
import com.example.hansung.ifindthanq.util.ProblemConfigurationVo;
import com.example.hansung.ifindthanq.util.SQLiteDBHelperDao;

import java.util.ArrayList;
import java.util.List;

public class AddBLEActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private String macs, name;
    private MyBLEAdapter myBLEAdapter;
    private List<MyBLE> myBLEList;
    private EditText bleName;

    private Bitmap bt;
    private String resultIcon;

    private InputMethodManager imm; //키보드 화면터치시 내려가게 설정

    private SQLiteDBHelperDao mSQLiteDBHelperDao = null;  //객체선언


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ble);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

//        TextView textView = (TextView) findViewById(R.id.textView);
        TextView myBLEMac = (TextView) findViewById(R.id.myBLEMac);
        TextView myname = (TextView) findViewById(R.id.name);

        bleName = (EditText) findViewById(R.id.bleName);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);

        Intent intent = getIntent();
        if (intent != null) {
            bt = (Bitmap) intent.getExtras().get("img");
            if (bt != null) {
                imageView.setImageBitmap(bt);
            }
            resultIcon = intent.getStringExtra("resultIcon");
//        System.out.print("result>>> " + resultIcon);

            if (resultIcon != null) {
                if (resultIcon.equals("1")) imageView.setImageResource(R.drawable.dogicon);
                else if (resultIcon.equals("2"))
                    imageView.setImageResource(R.drawable.headphoneicon);
                else if (resultIcon.equals("3"))
                    imageView.setImageResource(R.drawable.bluetoothicon);
                else if (resultIcon.equals("4"))
                    imageView.setImageResource(R.drawable.passport);
            }

            //CustomerAdapter에서 넘어온 데이터
            macs = intent.getStringExtra("macs");
            name = intent.getStringExtra("name");
            if (macs != null) {
                myBLEMac.setText(macs);
                Toast.makeText(this, "[" + name + "]", Toast.LENGTH_SHORT).show();
                if (name.equals("")) {
                    name = "(지정되지 않음)";
                }
                myname.setText(name);
            }
        }

        //image 고르는 Activity
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ADDBLEImageSearchPOP.class);
                intent.putExtra("macs", "" + macs);
                intent.putExtra("name", "" + name);
                startActivity(intent);
            }
        });

    }

    //추가하기 버튼 눌려서 MainActivity로 이동함.
    public void onAddClick(View view) {
        //추가 클릭시 (이미지, 아이콘, 맥주소, 사용자가 지정한 이름)을 추가시켜야함!

        //1. DB에 MyBLE(이미지, 아이콘, 맥주소, 사용자가 지정한 블루투스 제품 이름) 저장
        //2. MainActivity로 인텐트해서 넘어갔을때,
        //   myBLEList.add(new MyBLE(이미지/아이콘, 맥주소, 사용자가 지정한 블루투스 제품 이름));
        //3. MainActivity에서 DB 저장된 값들 불러오기

        Intent intent = new Intent(this, MainActivity.class);

        //사진이랑 아이콘
        intent.putExtra("img", bt);
        intent.putExtra("resultIcon", resultIcon);
//
//        intent.putExtra(macs, "" + macs);

        String name = bleName.getText().toString(); //edittext에서 받아온 bleName 값을 String으로 변환
        intent.putExtra("bleName", "" + name);

        System.out.print("[img] " + bt +"[resultIcon] " + resultIcon +", [Mac주소] " + macs + ", [등록할 bleName] " + name);
        Toast.makeText(this, "[img] " + bt +"[resultIcon] " + resultIcon +"[Mac주소] " + macs + ", [등록할 bleName] " + name, Toast.LENGTH_SHORT).show();

        mSQLiteDBHelperDao = new SQLiteDBHelperDao(getBaseContext());
        //ProblemConfigurationVo problem=new ProblemConfigurationVo(seq, type, bt, resultIcon, macs,  bleName);
       // mSQLiteDBHelperDao.addConfiguration(mCount , problem); //ArrayList 데이터 추가

        startActivity(intent);

        //MYBLE 리스트에 등록해서 MainActivity의 recyclerview에 보여줘야함.
//        List<MyBLE> myBLEList = null;
//        myBLEList.add(0,new MyBLE(1, "" + macs, "실험용"));
//        MyBLEAdapter myBLEAdapter = null;
//        myBLEAdapter.notifyItemInserted(0);
    }

    //키보드- 바탕화면 클릭시 내려가게 설정
    public void linearOnClick(View view) {
        imm.hideSoftInputFromWindow(bleName.getWindowToken(), 0);
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