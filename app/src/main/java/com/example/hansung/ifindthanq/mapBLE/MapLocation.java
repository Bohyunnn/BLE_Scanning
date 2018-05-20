package com.example.hansung.ifindthanq.mapBLE;

/**
 * Created by kimbohyun on 2018-05-14.
 */

public class MapLocation {

    private String time; //시간
    private String ble_name; //기기이름
    private double latitude; //위도
    private double longitude; //경도

    public MapLocation() {
    }

    public MapLocation(String ble_name, String time ,double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.ble_name = ble_name;
        this.time = time;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBle_name() {
        return ble_name;
    }

    public void setBle_name(String ble_name) {
        this.ble_name = ble_name;
    }

}
