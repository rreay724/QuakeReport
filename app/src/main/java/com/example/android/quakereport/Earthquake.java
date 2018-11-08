package com.example.android.quakereport;

public class Earthquake {

    private double mMagnitude;
    private String mLocation;
    private long mTimeInMilliseconds;
    private String mEarthquakeUrl;

    public Earthquake (double magnitude, String location, long time, String earthquakeUrl){
        mMagnitude = magnitude;
        mLocation = location;
        mTimeInMilliseconds = time;
        mEarthquakeUrl = earthquakeUrl;
    }

    public double getMagnitude() {
        return mMagnitude;
    }

    public String getLocation() {
        return mLocation;
    }

    public long getTimeInMilliseconds() {
        return mTimeInMilliseconds;
    }

    public String getEarthquakeUrl(){
        return mEarthquakeUrl;
    }
}
