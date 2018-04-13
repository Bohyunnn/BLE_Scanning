package com.example.hansung.ifindthanq.model;

import android.bluetooth.BluetoothDevice;

/* 사용자가 저장한 BLE 객체
 * @BLEImage: BLE 등록 이미지
 * @BLEName: BLE 이름
 * @BluetoothDevice: BLE 장치명(name, address)
 */

public class MyBLE {
    private int bleImage;
    private String macs;
    private String bleName;

    public MyBLE() {

    }

    public MyBLE(int bleImage, String macs, String bleName) {
        this.bleImage = bleImage;
        this.macs = macs;
        this.bleName = bleName;
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

    public String getMacs() {
        return macs;
    }

    public void setMacs(String macs) {
        this.macs = macs;
    }


}
