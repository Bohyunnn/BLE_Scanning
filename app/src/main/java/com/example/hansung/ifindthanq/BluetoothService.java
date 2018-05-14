package com.example.hansung.ifindthanq;

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
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.hansung.ifindthanq.Main.MainActivity;
import com.example.hansung.ifindthanq.mapBLE.MapLocation;
import com.example.hansung.ifindthanq.model.NearBLE;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BluetoothService extends Service {
    //onCreate-> onStartCommand-> Service Running -> onDestory 순으로 진행됨.

    NotificationManager notificationManager;
    ServiceThread thread;
    Notification notification;
    private MapLocation mapLocation;
    private LocationManager lm;


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

            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mReceiver, filter);


//            //notification 알람
//            Intent intent = new Intent(BluetoothService.this, MainActivity.class);
//            PendingIntent pendingIntent = PendingIntent.getActivity(BluetoothService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//            Resources res = getResources();
//
//            notification = new Notification.Builder(getApplicationContext())
//                    .setContentTitle("블루투스 제품과 거리가 멀어요.") //제목
//                    .setContentText("블루투스 제품 값 / mac 주소 값")  //내용
//                    .setSmallIcon(R.drawable.bluetoothicon)
//                    .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.bluetoothicon))  //아이콘
//                    .setTicker("블루투스 제품 분실 위험이 있어요!")    //알람 내용
//                    .setContentIntent(pendingIntent)
//                    .setWhen(System.currentTimeMillis())
//                    .build();
//
//            //소리, 진동 추가
//            notification.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
//
//            //알림 소리를 한번만 내도록
//            notification.flags = Notification.FLAG_ONLY_ALERT_ONCE;
//
//            //확인하면 자동으로 알림이 제거 되도록
//            notification.flags = Notification.FLAG_AUTO_CANCEL;
//
//            //노티피케이션의 고유아이디 777로 설정
//            notificationManager.notify(777, notification);
//
//            //토스트 띄우기
//            Toast.makeText(BluetoothService.this, "뜸?", Toast.LENGTH_LONG).show();
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

                String registerDevice = "CC:44:63:42:F6:C0"; //임시로 맥 주소! //hc-06
                //String registerDevice = "98:D3:32:70:B0:73"; //내 핸드폰 맥 주소

                if (searchDevice.equals(registerDevice)) {
                    Toast.makeText(BluetoothService.this, "[rssi 값 1]= " + rssi + " 거리는 =" + dist, Toast.LENGTH_LONG).show();
                    Log.d("[rssi 값 1]", "= " + rssi + " 거리는 =" + dist);

                    //거리가 0이상이면 알람발생
                    if (dist > 0) {
                        Toast.makeText(BluetoothService.this, "[rssi 값 2]= " + rssi + " 거리는 =" + dist, Toast.LENGTH_LONG).show();
                        Toast.makeText(BluetoothService.this, "뜸?", Toast.LENGTH_LONG).show();
                        Log.d("[rssi 값 2]", "= " + rssi + " 거리는 =" + dist);

                        //현재 위치 지도 찍어주기!!!!!!!!!!!!!!
                        //맵sqlite > [registerName, registerDevice(mac), longitude, latitude, Date]

                        //맵 등록 (BluetoothService.java)
                        //  1. 맵에 등록될때, (registerName, registerDevice) 존재하면 => update
                        //  2.                                              존재하지 않으면 => insert

                        try {
                            // GPS 제공자의 정보가 바뀌면 콜백하도록 리스너 등록하기~!!!
                            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치제공자
                                    100, // 통지사이의 최소 시간간격 (miliSecond)
                                    1, // 통지사이의 최소 변경거리 (m)
                                    mLocationListener);
                            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자
                                    100, // 통지사이의 최소 시간간격 (miliSecond)
                                    1, // 통지사이의 최소 변경거리 (m)
                                    mLocationListener);
                        } catch (SecurityException ex) {
                        }


                        //토스트 띄우기
                        intent = new Intent(BluetoothService.this, MainActivity.class);
                        PendingIntent pendingIntent = PendingIntent.getActivity(BluetoothService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        Resources res = getResources();

                        //노티피케이션 발생
                        notification = new Notification.Builder(getApplicationContext())
                                .setContentTitle("블루투스 제품과 거리가 멀어요.") //제목
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
                        Toast.makeText(BluetoothService.this, "뜸?", Toast.LENGTH_LONG).show();

                    }
                }

            }
        }
    };

    LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            //여기서 위치값이 갱신되면 이벤트가 발생한다.
            //값은 Location 형태로 리턴되며 좌표 출력 방법은 다음과 같다.

            Log.d("test", "onLocationChanged, location:" + location);
            double longitude = location.getLongitude(); //경도
            double latitude = location.getLatitude();   //위도

            Log.i("location >>>> ", "[" + longitude + " ," + latitude + "]");
            System.out.println("location >>>>  [" + longitude + " ," + latitude + "]");
            //위도 경도 MapLocation에 설정
            mapLocation.setLongitude(longitude);
            mapLocation.setLatitude(latitude);

        }

        public void onProviderDisabled(String provider) {
            // Disabled시
            Log.d("test", "onProviderDisabled, provider:" + provider);
        }

        public void onProviderEnabled(String provider) {
            // Enabled시
            Log.d("test", "onProviderEnabled, provider:" + provider);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            // 변경시
            Log.d("test", "onStatusChanged, provider:" + provider + ", status:" + status + " ,Bundle:" + extras);
        }
    };


}
