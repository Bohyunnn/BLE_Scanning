package com.example.hansung.ifindthanq;

/**
 * Created by kimsungmin on 2018-04-13.
 */

public class MarkerItem {

    double lat;
    double lon;

    public MarkerItem(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;

    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }


}
