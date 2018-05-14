package com.example.hansung.ifindthanq.mapBLE;

/**
 * Created by kimsungmin on 2018-05-14.
 */

public class MapLocation {

    private double latitude; //위도
    private double longitude; //경도

    public MapLocation() {
    }

    public MapLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
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
}
