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
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.hansung.ifindthanq.Main.MainActivity;
import com.example.hansung.ifindthanq.model.NearBLE;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BluetoothService extends Service {
    //onCreate-> onStartCommand-> Service Running -> onDestory 순으로 진행됨.

    NotificationManager notificationManager;
    ServiceThread thread;
    Notification notification;

    public BluetoothService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //최초 생성 되었을때 한번 실행됨.
    @Override
    public void onCreate() {
        super.onCreate();
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

                String registerDevice = "CC:44:63:42:F6:C0"; //임시로 맥 주소!

                if (searchDevice.equals(registerDevice)) {
                    Toast.makeText(BluetoothService.this, "[rssi 값 1]= " + rssi + " 거리는 =" + dist, Toast.LENGTH_LONG).show();
                    Log.d("[rssi 값 1]", "= " + rssi + " 거리는 =" + dist);
                    if (dist > 0) {
                        Toast.makeText(BluetoothService.this, "[rssi 값 2]= " + rssi + " 거리는 =" + dist, Toast.LENGTH_LONG).show();
                        Toast.makeText(BluetoothService.this, "뜸?", Toast.LENGTH_LONG).show();
                        Log.d("[rssi 값 2]", "= " + rssi + " 거리는 =" + dist);
                        //노티피케이션 발생 + 지도 찍어주기?
                        //토스트 띄우기
                        intent = new Intent(BluetoothService.this, MainActivity.class);
                        PendingIntent pendingIntent = PendingIntent.getActivity(BluetoothService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        Resources res = getResources();

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


}
