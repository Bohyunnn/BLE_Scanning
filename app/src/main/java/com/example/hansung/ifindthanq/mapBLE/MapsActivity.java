package com.example.hansung.ifindthanq.mapBLE;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.hansung.ifindthanq.R;
import com.example.hansung.ifindthanq.util.SQLiteDBHelperDao;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ArrayList<MapLocation> locationList;
    private SQLiteDBHelperDao mSQLiteDBHelperDao = null;

    private GoogleMap mMap;
    private double mLatitude = 37.581764;
    private double mLongitude = 127.010326;
    private MapLocation mapLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("블루투스 꺼진 곳 위치");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //기본 생성(위치)
        //mapLocation = new MapLocation(mLatitude, mLongitude);

        mSQLiteDBHelperDao = new SQLiteDBHelperDao(this);

        FragmentManager fragmentManager = getFragmentManager();

        MapFragment mapFragment = (MapFragment) fragmentManager

                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap map) {

        //위치 데이터 불러옴
        locationList = mSQLiteDBHelperDao.getConfigurationsLocAll();


        //      위치 데이터들을 순차적으로 마킹해준다.
        for (int i = 0; i < locationList.size(); i++) {
            Log.e("locationList", locationList.get(i).getBle_name());

            LatLng location = new LatLng(locationList.get(i).getLatitude(), locationList.get(i).getLongitude());

            String locationName = getAddress(getApplicationContext(), locationList.get(i).getLatitude(), locationList.get(i).getLongitude());

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(location);
            markerOptions.title("[" + locationList.get(i).getBle_name() + "] "+ locationList.get(i).getTime());
            markerOptions.snippet(locationName);

            map.addMarker(markerOptions);
        }

        LatLng location = new LatLng(mLatitude, mLongitude);

        //화면중앙의 위치와 카메라 줌비율
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 13));

    }

    /**
     * 위도,경도로 주소구하기
     *
     * @param lat
     * @param lng
     * @return 주소
     */
    public static String getAddress(Context mContext, double lat, double lng) {
        String nowAddress = "현재 위치를 확인 할 수 없습니다.";
        Geocoder geocoder = new Geocoder(mContext, Locale.KOREA);
        List<Address> address;
        try {
            if (geocoder != null) {
                //세번째 파라미터는 좌표에 대해 주소를 리턴 받는 갯수로
                //한좌표에 대해 두개이상의 이름이 존재할수있기에 주소배열을 리턴받기 위해 최대갯수 설정
                address = geocoder.getFromLocation(lat, lng, 1);

                if (address != null && address.size() > 0) {
                    // 주소 받아오기
                    String currentLocationAddress = address.get(0).getAddressLine(0).toString();
                    nowAddress = currentLocationAddress;

                }
            }

        } catch (IOException e) {
            Toast.makeText(mContext, "주소를 가져 올 수 없습니다.", Toast.LENGTH_LONG).show();

            e.printStackTrace();
        }
        return nowAddress;
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
