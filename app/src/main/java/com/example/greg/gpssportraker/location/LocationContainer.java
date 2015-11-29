package com.example.greg.gpssportraker.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.widget.Toast;

import com.example.greg.gpssportraker.service.LocationService;
import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;

/**
 * Created by Geerg on 2015.11.23..
 */
public class LocationContainer {
    private ArrayList<LatLng> myroute = new ArrayList<>();
    //private magassagok
    //private sebessegek
    //private? egyéb?
    private Location newLocation;

    public void addPoint(LatLng location){
        myroute.add(location);
    }

    //csak tesztelés!!!
    public LatLng getPoint() {
        int lastindex = myroute.size();
        return myroute.get(lastindex);
    }




}
