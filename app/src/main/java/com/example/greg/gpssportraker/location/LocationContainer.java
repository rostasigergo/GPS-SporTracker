package com.example.greg.gpssportraker.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.widget.Toast;

import com.example.greg.gpssportraker.service.LocationService;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.orm.SugarRecord;

import java.util.ArrayList;

/**
 * Created by Geerg on 2015.11.23..
 */
public class LocationContainer extends SugarRecord{
    private int sorszam;
    private ArrayList<LatLng> myroute = new ArrayList<>();
    // private ArrayList<PolylineOptions> mycolors = new ArrayList<PolylineOptions>();
    private PolylineOptions poptions = new PolylineOptions();
    private double Magassag[] = new double[1];
    private float Speeds[] = new float[1];

    public LocationContainer(){
    }

    public LocationContainer(int sorszam){
        this.sorszam = sorszam;
        //konstuktor
    }
    public void addNewLocation(){
        
    }

    private Location newLocation;

    public void addPoint(LatLng location){
        myroute.add(location);
    }

    public LatLng getPoint() {
        int lastindex = myroute.size();
        return myroute.get(lastindex);
    }

}
