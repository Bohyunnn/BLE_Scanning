package com.example.hansung.ifindthanq;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Created by kimbohyun on 2018-05-31.
 */

public class BluetoothConnectService extends Service {
    private BluetoothAdapter mBluetoothAdapter;
    static final int REQUEST_ENABLE_BT = 10;

    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private ConnectedThread mConnectedThread;
    final int RECIEVE_MESSAGE = 1;        // Status  for Handler
    Handler h;

    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 서비스에서 가장 먼저 호출됨(최초에 한번만)

    }

    // 서비스가 호출될 때마다 실행
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        checkBTState(); //블루투스 비활성화시=> 활성화 시킴

        String bleMac = intent.getStringExtra("blaMac");

        System.out.println(">>>>>>>>>BluetoothConnectService 1>>>>>>>>>>>> " + bleMac);
        connectToSelectedDevice(bleMac);

//        for (int i = 0; i < 1000; i++) {
//            mConnectedThread.write("1");
//        }

        System.out.println(">>>>>>>>>BluetoothConnectService 2>>>>>>>>>>>> " + bleMac);
        //Toast.makeText(getApplicationContext(), bleMac.toString() + "연결하기 완료", Toast.LENGTH_SHORT).show();


        return START_STICKY;
    }

    private void connectToSelectedDevice(String bleMac) {
        Log.d("BLEDistanceActivity>>", "...onResume - try connect...");

        BluetoothDevice device = btAdapter.getRemoteDevice(bleMac);

        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            errorExit("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + ".");
        }


        // Discovery is resource intensive.  Make sure it isn't going on
        // when you attempt to connect and pass your message.
        btAdapter.cancelDiscovery();

        // Establish the connection.  This will block until it connects.
        Log.d("BLEDistanceActivity>>", "...Connecting...");
        Toast.makeText(getBaseContext(), "연결중 입니다.", Toast.LENGTH_SHORT).show();
        try {
            btSocket.connect();
            Log.d("BLEDistanceActivity>>", "....Connection ok...");
            Toast.makeText(getBaseContext(), "연결 되었습니다.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            try {
                btSocket.close();
                Toast.makeText(getBaseContext(), "연결에 실패했습니다.", Toast.LENGTH_SHORT).show();
            } catch (IOException e2) {
                errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
                Toast.makeText(getBaseContext(), "연결에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        }

        // Create a data stream so we can talk to server.
        Log.d("BLEDistanceActivity>>", "...Create Socket...");

        mConnectedThread = new ConnectedThread(btSocket);
        mConnectedThread.start();

    }

    private void errorExit(String title, String message) {
        Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_LONG).show();
    }

    //블루투스 활성화 부분1
    private void checkBTState() {
        // Check for Bluetooth support and then check to make sure it is turned on
        // Emulator doesn't support Bluetooth and will return null
        if (btAdapter == null) {
            errorExit("Fatal Error", "Bluetooth not support");
        } else {
            if (btAdapter.isEnabled()) {
                Log.d("BLEDistanceAcitivy>>>", "...Bluetooth ON...");
            }
        }
    }


    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        if (Build.VERSION.SDK_INT >= 10) {
            try {
                final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[]{UUID.class});
                return (BluetoothSocket) m.invoke(device, MY_UUID);
            } catch (Exception e) {
                Log.e("BLEDistanceActivity>>", "Could not create Insecure RFComm Connection", e);
            }
        }
        return device.createRfcommSocketToServiceRecord(MY_UUID);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("BLEDistanceActivity>>", "...In onPause()...");
        Toast.makeText(getBaseContext(), "연결에 실패했습니다.", Toast.LENGTH_SHORT).show();

        try {
            btSocket.close();
        } catch (IOException e2) {
            errorExit("Fatal Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");
            Toast.makeText(getBaseContext(), "연결에 실패했습니다.", Toast.LENGTH_SHORT).show();
        }

    }

    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            while (true) {
                mConnectedThread.write("1");
            }
        }

        //안드-> 아두이노 데이터 전달
        public void write(String message) {
            // Log.d("BLEDistanceActivity>>", "...Data to send: " + message + "...");
            byte[] msgBuffer = message.getBytes();
            try {
                mmOutStream.write(msgBuffer);
            } catch (IOException e) {
                //   Log.d("BLEDistanceActivity>>", "...Error data send: " + e.getMessage() + "...");
            }
        }
    }


}

