package com.example.robert.carpark.models;

/**
 * Created by Robert on 13.06.2018.
 */

public class Activity {
    private String ActivityName;
    private double latitude;
    private double longitude;

    public Activity(String ActivityName, double latitude, double longitude) {
        this.ActivityName = ActivityName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Activity() {}

    public String getActivityName() { return ActivityName; }

    public void setActivityName(String ActivityName) { this.ActivityName = ActivityName;}

    public double getLatitude() { return latitude; }

    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}

