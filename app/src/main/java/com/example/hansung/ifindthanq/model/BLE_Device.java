package com.example.hansung.ifindthanq.model;

import android.bluetooth.BluetoothDevice;

/* BLEDevice와 RSSI 저장하는 객체
 * @BluetoothDevice: BLE 장치(name, address)
 * @RSSI: 강도세기
 */
public class BLE_Device {

    private BluetoothDevice bluetoothDevice;
    private int rssi;

    public BLE_Device(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }

    public String getAddress() {
        return bluetoothDevice.getAddress();
    }

    public String getName() {
        return bluetoothDevice.getName();
    }

    public void setRSSI(int rssi) {
        this.rssi = rssi;
    }

    public int getRSSI() {
        return rssi;
    }
}
