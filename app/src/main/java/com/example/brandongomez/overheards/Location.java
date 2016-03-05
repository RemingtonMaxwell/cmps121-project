package com.example.brandongomez.overheards;

/**
 * Created by remin on 3/3/2016.
 */
public class Location{
    private double latitude;
    private double longitude;
    public Location() {
        // empty default constructor, necessary for Firebase to be able to deserialize locations
    }
    public Location(double latitude, double longitude){
        this.latitude=latitude;
        this.longitude=longitude;
    }

    /******************Getter Methods for Locations*************************/

    public double getLatitude(){
        return latitude;
    }
    public double getLongitude(){
        return  longitude;
    }

}

