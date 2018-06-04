package com.example.hansung.ifindthanq;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.hansung.ifindthanq.Main.MainActivity;
import com.example.hansung.ifindthanq.mapBLE.MapLocation;
import com.example.hansung.ifindthanq.model.NearBLE;
import com.example.hansung.ifindthanq.util.ProblemConfigurationVo;
import com.example.hansung.ifindthanq.util.SQLiteDBHelperDao;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class BluetoothService extends Service {
    //onCreate-> onStartCommand-> Service Running -> onDestory 순으로 진행됨.

    NotificationManager notificationManager;
    ServiceThread thread;
    Notification notification;
    private MapLocation mapLocation;
    private LocationManager lm;

    private SQLiteDBHelperDao mSQLiteDBHelperDao = null;  //객체선언

    private double mLatitude = 37.581764;
    private double mLongitude = 127.010326;

    protected LocationCallback mLocationCallback;
    private static final int RC_LOCATION = 1;
    private static final int RC_LOCATION_UPDATE = 2;
    protected static final String TAG = "MainActivity";
    protected Location mLastLocation;
    FusedLocationProviderClient mFusedLocationClient;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //최초 생성 되었을때 한번 실행됨.
    @Override
    public void onCreate() {
        super.onCreate();
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mapLocation = new MapLocation();

        mSQLiteDBHelperDao = new SQLiteDBHelperDao(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

    }

    //백그라운드에서 실행되는 동작들
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        myServiceHandler handler = new myServiceHandler();
        thread = new ServiceThread(handler);
        thread.start();
        return START_STICKY;
    }

    //서비스가 종료될때 실행되는 함수
    @Override
    public void onDestroy() {
        thread.stopForever();
        thread = null;//쓰레기 값을 만들어서 빠르게 회수하라고 null을 넣어줌.
    }


    class myServiceHandler extends Handler {
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(android.os.Message msg) {
            // Register the BroadcastReceiver
            Intent intent = new Intent(BluetoothService.this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(BluetoothService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mReceiver, filter);

            Log.e("meter", String.valueOf(mSQLiteDBHelperDao.getConfigurationsMeter()));
        }
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        public void onReceive(Context context, Intent intent) {
            int dist = 0;
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
                if (rssi > -45) {
                    dist = 0;
                } else if (-45 > rssi && rssi >= -48)
                    dist = 1;
                else if (-48 > rssi && rssi >= -51)
                    dist = 2;
                else if (-51 > rssi && rssi >= -54)
                    dist = 3;
                else if (-54 > rssi && rssi >= -58)
                    dist = 4;
                else if (-58 > rssi && rssi >= -61)
                    dist = 5;
                else if (-61 > rssi && rssi >= -68)
                    dist = 6;
                else if (-68 > rssi && rssi >= -71)
                    dist = 7;
                else if (-71 > rssi && rssi >= -76)
                    dist = 8;
                else if (-76 > rssi && rssi >= -80)
                    dist = 9;
                else if (-80 > rssi)
                    dist = 10;


                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                String searchDevice = device.getAddress();

                String registerDevice;

                List<ProblemConfigurationVo> listBluetooth = mSQLiteDBHelperDao.getConfigurationsAll();

                for (ProblemConfigurationVo problemConfigurationVo : listBluetooth) {
                    Log.d("BluetoothService", "비교 Mac 주소>>" + problemConfigurationVo.getBleName() + "," + problemConfigurationVo.getMacs());
                    registerDevice = problemConfigurationVo.getMacs();


                    if (searchDevice.equals(registerDevice)) {
//                        Toast.makeText(BluetoothService.this, "[rssi 값 1]= " + rssi + " 거리는 =" + dist, Toast.LENGTH_LONG).show();
//                        Log.d("[BS-rssi 값 1]", "= " + rssi + " 거리는 =" + dist);
                        System.out.println("[BS-rssi 값 1] >>>>>>>>  " + rssi + " 거리는 =" + dist);

                        int meter = mSQLiteDBHelperDao.getConfigurationsMeter();
                        System.out.println("BletoothService Meter>>>>>>>>  " + meter);
                        System.out.println("BletoothService dist>>>>>>>>  " + dist);

                        //거리가 0이상이면 알람발생
                        if (dist >= meter) {
                            //Toast.makeText(BluetoothService.this, "[rssi 값 2]= " + rssi + " 거리는 =" + dist, Toast.LENGTH_LONG).show();

                            Log.d("[BS-rssi 값 2]", "= " + rssi + " 거리는 =" + meter);
                            //위치값 찍어주기
                            getLastLocation();

                            Log.d("[BS-location 값]", mLatitude + ", " + mLongitude);
                            //          insert location
                            MapLocation location = new MapLocation(problemConfigurationVo.getBleName(), getNowTime(), mLatitude, mLongitude);

                            //            위치정보 db에 추가
                            mSQLiteDBHelperDao.addConfigurationLoc(location);

                            //토스트 띄우기
                            intent = new Intent(BluetoothService.this, MainActivity.class);
                            PendingIntent pendingIntent = PendingIntent.getActivity(BluetoothService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                            Resources res = getResources();

                            //노티피케이션 발생
                            notification = new Notification.Builder(getApplicationContext())
                                    .setContentTitle("[" + problemConfigurationVo.getBleName() + "] 블루투스 제품과 거리가 멀어요.") //제목
                                    .setContentText(registerDevice)  //Mac 주소
                                    .setSmallIcon(R.drawable.bluetoothicon)
                                    .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.bluetoothicon))  //아이콘
                                    .setTicker("블루투스 제품 분실 위험이 있어요!")    //알람 내용
                                    .setContentIntent(pendingIntent)
                                    .setWhen(System.currentTimeMillis())
                                    .build();

                            //소리, 진동 추가
                            notification.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;

                            //알림 소리를 한번만 내도록
                            notification.flags = Notification.FLAG_ONLY_ALERT_ONCE;

                            //확인하면 자동으로 알림이 제거 되도록
                            notification.flags = Notification.FLAG_AUTO_CANCEL;

                            //노티피케이션의 고유아이디 777로 설정
                            notificationManager.notify(777, notification);

                            //토스트 띄우기
//                            Toast.makeText(BluetoothService.this, "뜸?", Toast.LENGTH_LONG).show();
                            Log.d("[BS-notification]", rssi + " 거리는 =" + dist);
                        }
                    }

                }
            }
        }
    };


    private String getNowTime() {
        String time = null;

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yy/MM/dd hh:mm:ss");
        time = sdf1.format(cal.getTime());

        return time;
    }


    //-------------------------------
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
//
//    }

    @SuppressWarnings("MissingPermission")
    @AfterPermissionGranted(RC_LOCATION_UPDATE)
    public void startLocationUpdate() {
        LocationRequest locRequest = new LocationRequest();
        locRequest.setInterval(3000);
        locRequest.setFastestInterval(1000);
        locRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                getLastLocation();
            }
        };


        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            mFusedLocationClient.requestLocationUpdates(locRequest, mLocationCallback, Looper.myLooper());
        } else {
//            // Do not have permissions, request them now
//            EasyPermissions.requestPermissions(this,
//                    "This app needs access to your location to know where you are.",
//                    RC_LOCATION_UPDATE, perms);
        }
    }

    public void stopLocationUpdate() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    @SuppressWarnings("MissingPermission")
    @AfterPermissionGranted(RC_LOCATION)
    public void getLastLocation() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};

        if (EasyPermissions.hasPermissions(this, perms)) {
            mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        mLastLocation = task.getResult();
                        try {
                            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.KOREA);
                            List<Address> addresses = geocoder.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1);
                            if (addresses.size() > 0) {
                                Address address = addresses.get(0);
                                mLongitude = address.getLongitude();
                                mLatitude = address.getLatitude();
                            }
                        } catch (IOException e) {
                            Log.e(TAG, "Failed in using Geocoder", e);
                        }
                    }
                }
            });
        } else {
            //EasyPermissions.requestPermissions(this, "This app needs access to your location to know where you are", RC_LOCATION, perms);
        }
    }
}