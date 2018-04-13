package com.example.hansung.ifindthanq.model;

import java.util.Date;

/* BLE 근처 블루투스 rssi 값 가져오는 데이터 모델
 * @bleName: BLE 검색 이름
 * @macAddress: BLE 검색 Mac 주소
 * @update : 시간
 * @rssi : ble rssi 값
 */
public class NearBLE {
    private String bleName;
    private String macAddress;
    private String date;
    private String rssi;
    private String distance;

    public NearBLE() {
    }

    public NearBLE(String bleName, String macAddress, String date, String rssi, String distance) {
        this.bleName = bleName;
        this.macAddress = macAddress;
        this.date = date;
        this.rssi = rssi;
        this.distance = distance;
    }

    public String getBleName() {
        return bleName;
    }

    public void setBleName(String bleName) {
        this.bleName = bleName;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRssi() {
        return rssi;
    }

    public void setRssi(String rssi) {
        this.rssi = rssi;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
