package com.example.brandongomez.cs121asg3;

import android.location.Location;

/**
 * Created by brandongomez on 2/8/16.
 */
public class LocationData {

    private static LocationData instance = null;

    private LocationData(){}

    private Location location;


    public Location getLocation(){
        return location;
    }

    public void setLocation(Location _location){
        location = _location;
    }

    public static LocationData getLocationData(){
        if(instance == null){
            instance = new LocationData();
        }
        return instance;
    }
}
