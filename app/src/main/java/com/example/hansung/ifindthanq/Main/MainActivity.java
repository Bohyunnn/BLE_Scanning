package com.example.hansung.ifindthanq.Main;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hansung.ifindthanq.BluetoothService;
import com.example.hansung.ifindthanq.mapBLE.MapsActivity;
import com.example.hansung.ifindthanq.BLESettingActivity;
import com.example.hansung.ifindthanq.nearBLE.NearDistanceBLEActivity;
import com.example.hansung.ifindthanq.R;
import com.example.hansung.ifindthanq.addBLE.BLESearchActivity;

import com.example.hansung.ifindthanq.util.ProblemConfigurationVo;
import com.example.hansung.ifindthanq.util.SQLiteDBHelperDao;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
//    private FrameLayout frameLayout;

    private RecyclerView recyclerView;
    private MyBLEAdapter myBLEAdapter;
    private List<ProblemConfigurationVo> myBLEList;

    private static final int REQUEST_ENABLE_BT = 123456789;
    private BluetoothAdapter bluetoothAdapter = null;

    private SQLiteDBHelperDao mSQLiteDBHelperDao = null;  //객체선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSQLiteDBHelperDao = new SQLiteDBHelperDao(this);

        // BluetoothAdapter 인스턴스를 얻는다
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //toolbar 설정
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("IFindThanKQ");
        toolbar.setTitleTextColor(Color.WHITE);

        //+버튼 누를 시 작동
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //NavigationView 설정
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        recyclerView = (RecyclerView) findViewById(R.id.recycler);

        myBLEList = new ArrayList<ProblemConfigurationVo>();
        myBLEAdapter = new MyBLEAdapter(this, myBLEList);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(25), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(myBLEAdapter);

        prepareAlbums();

        try {
            Glide.with(this).load(R.mipmap.ic_launcher).into((ImageView) findViewById(R.id.bleImage));
        } catch (Exception e) {
//            e.printStackTrace();
        }

    }

//    //뒤로가기 눌렀을때 DrawerLayout 작동
//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //메뉴 옵션
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_bluetooth) {//왼쪽 상단바 bluetooth 클릭시
            if (bluetoothAdapter == null) {
                // 단말기는 Bluetooth를 지원하지 않음.
            } else {
                //bluetooth 지원하는 경우
                if (!bluetoothAdapter.isEnabled()) {//블루투스 활성화되지 않은 경우
                    //블루투스 활성화하기
                    Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(turnOn, 0);
                    Toast.makeText(getApplicationContext(), "Bluetooth Turned on", Toast.LENGTH_LONG).show();
                } else {//블루투스 활성화되어 있는 경우
                    //블루투스 비활성화하기
                    bluetoothAdapter.disable();
                    Toast.makeText(getApplicationContext(), "Bluetooth Turned off", Toast.LENGTH_LONG).show();
                }
            }
            return true;
        } else if (id == R.id.action_notification) {
            Toast.makeText(getApplicationContext(), "notification Service 실행", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this, BluetoothService.class);
            startService(intent);
            return true;
        } else if (id == R.id.action_noNotification) {
            Toast.makeText(getApplicationContext(), "notification Service 중지", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this, BluetoothService.class);
            stopService(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Navigation Bar's Intent
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_myBLE) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_addMyBLE) {
            Intent intent = new Intent(this, BLESearchActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_searchBLE) {
            Intent intent = new Intent(this, NearDistanceBLEActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_mapBLE) {
            //Intent intent = new Intent(this, BLEMapActivity.class);
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_settingBlue) {
            //
        } else if (id == R.id.nav_settingGPS) {

        } else if (id == R.id.nav_settingBLE) {

            Intent intent = new Intent(this, BLESettingActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_contact) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void prepareAlbums() {
        //등록된 블루투스 DB 가져오기
        myBLEList.addAll(mSQLiteDBHelperDao.getConfigurationsAll());

        //plus(+) 버튼
        ProblemConfigurationVo a = new ProblemConfigurationVo( null ,R.drawable.plusicon, null, " ");
        myBLEList.add(a);

        myBLEAdapter.notifyDataSetChanged();
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
