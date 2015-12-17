package com.example.greg.gpssportraker.location;

import android.graphics.Color;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Geerg on 2015.11.23..
 */
public class LocationContainer extends SugarRecord{
    private int sorszam;
    private Date datum;
    private float distance;
    private ArrayList<LatLng> myroute = new ArrayList<>();
    private PolylineOptions poptions = new PolylineOptions();// private ArrayList<PolylineOptions> mycolors = new ArrayList<PolylineOptions>();
    private ArrayList<Double> magassagok = new ArrayList<>();
    private ArrayList<Float> speeds = new ArrayList<>();//private float Speeds[] = new float[1];
    private int arraylenght;

    public LocationContainer(){
    }

    public LocationContainer(int sorszam){
        this.sorszam = sorszam;
        datum = new Date();
        poptions.color(Color.RED);
        poptions.width(10);
        distance = 0;
    }
    public void addNewLocation(LatLng newloc, double magassag, float speed){
        myroute.add(newloc);
        magassagok.add(magassag);
        speeds.add(speed);
        arraylenght = myroute.size();
        poptions.add(newloc);
        calculateDistance();
    }

    Location loc1 = new Location("A");
    Location loc2 = new Location("B");
    private void calculateDistance(){
        if (arraylenght >= 2){
            loc1.setLatitude(myroute.get(arraylenght-2).latitude);
            loc1.setLongitude(myroute.get(arraylenght-2).longitude);

            loc2.setLatitude(myroute.get(arraylenght-1).latitude);
            loc2.setLongitude(myroute.get(arraylenght-1).longitude);

            distance += loc1.distanceTo(loc2);
        }
    }

    public int getSorszam(){
        return sorszam;
    }

    public int getLenght(){
        return arraylenght;
    }

    public float getDistance(){
        return distance;
    }

    public PolylineOptions getPolyline(){
        return poptions;
    }

    public LatLng getLastLatLng() {
        return myroute.get(arraylenght);
    }
    public double getLastMagassag() {
        return magassagok.get(arraylenght);
    }
    public float getLastSpeed() {
        return speeds.get(arraylenght);
    }

}
