package com.example.hansung.ifindthanq.model;

/**
 * Created by kimbohyun on 2018-03-15.
 */

import android.bluetooth.BluetoothDevice;

/* 사용자가 저장한 BLE 객체
 *
 * @BLEImage: BLE 등록 이미지
 * @BLEName: BLE 이름
 * @BluetoothDevice: BLE 장치명(name, address)
 *
 */
public class MyBLE {
    private int bleImage;
    private String bleName;
    private BluetoothDevice bluetoothDevice;

    public MyBLE() {

    }

    public MyBLE(int bleImage, String bleName) {
        this.bleImage = bleImage;
        this.bleName = bleName;
    }

    public MyBLE(int bleImage, String bleName, BluetoothDevice bluetoothDevice) {
        this.bleImage = bleImage;
        this.bleName = bleName;
        this.bluetoothDevice = bluetoothDevice;
    }

    public int getBleImage() {
        return bleImage;
    }

    public void setBleImage(int bleImage) {
        this.bleImage = bleImage;
    }

    public String getBleName() {
        return bleName;
    }

    public void setBleName(String bleName) {
        this.bleName = bleName;
    }

    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }

    public void setBluetoothDevice(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }
}
