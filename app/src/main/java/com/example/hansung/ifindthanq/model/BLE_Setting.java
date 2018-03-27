package com.example.hansung.ifindthanq.model;

/**
 * Created by kimbohyun on 2018-03-15.
 */

/* BLEDevice Setting 객체
 *
 * @scanPeriod: 스캔주기
 * @signalStrength: 스캔강도
 *
 */

public class BLE_Setting {
    private long scanPeriod;
    private int signalStrength;

    public BLE_Setting() {
    }

    public BLE_Setting(long scanPeriod, int signalStrength) {
        this.scanPeriod = scanPeriod;
        this.signalStrength = signalStrength;
    }

    public long getScanPeriod() {
        return scanPeriod;
    }

    public void setScanPeriod(long scanPeriod) {
        this.scanPeriod = scanPeriod;
    }

    public int getSignalStrength() {
        return signalStrength;
    }

    public void setSignalStrength(int signalStrength) {
        this.signalStrength = signalStrength;
    }
}
