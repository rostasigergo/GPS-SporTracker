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
public class LocationContainer{
    private Date datum;
    private float distance;
    private double elevationgain;
    private ArrayList<LatLng> myroute = new ArrayList<>();
    private PolylineOptions poptions = new PolylineOptions();// private ArrayList<PolylineOptions> mycolors = new ArrayList<PolylineOptions>();
    private ArrayList<Double> magassagok = new ArrayList<>();
    private ArrayList<Float> speeds = new ArrayList<>();//private float Speeds[] = new float[1];
    private float avgspeed;
    private int arraylenght;
    private ArrayList<Float> accu = new ArrayList<>(); //pontosság


    public LocationContainer(){
        datum = new Date();
        poptions.color(Color.RED);
        poptions.width(10);
        distance = 0;
    }
    public void addNewLocation(LatLng newloc, double magassag, float speed, float acc){
        myroute.add(newloc);
        magassagok.add(magassag);
        speeds.add(speed);
        accu.add(acc);

        arraylenght = myroute.size();
        poptions.add(newloc);

        calculateDistance();
        avgspeed = calculateAverage(speeds);
        if (acc < 10) {
            elevationgain = calculateElevationGain(magassagok);
        }
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
    private float calculateAverage(ArrayList<Float> speeds) {
        Float sum = 0f;
        if(!speeds.isEmpty()) {
            for (Float speed : speeds) {
                sum += speed;
            }
            return sum / speeds.size();
        }
        return sum;
    }
    private double calculateElevationGain(ArrayList<Double> ertekek){
        double elevationgain = 0;
        double max = 0, min = 0;
        if (!ertekek.isEmpty()){
            for (int i=0; i< ertekek.size(); i++) {
                if (ertekek.get(i) > max && accu.get(i) <= 10f){//Pontosság figyelembe vétele!!
                    max = ertekek.get(i);
                }
                if (ertekek.get(i) < min && accu.get(i) <= 10f){
                    min = ertekek.get(i);
                }
            }
            elevationgain = max -min;
            return elevationgain;
        }
        return elevationgain;
    }

    public int getLenght(){
        return arraylenght;
    }

    public float getDistance(){
        return distance;
    }
    public double getElevationgain(){
        return elevationgain;
    }

    public PolylineOptions getPolyline(){
        return poptions;
    }

    public LatLng getLastLatLng() {
        return myroute.get(arraylenght-1);
    }
    public double getLastMagassag() {
        return magassagok.get(arraylenght-1);
    }
    public float getLastSpeed() {
        return speeds.get(arraylenght-1);
    }
    public float getAvgspeed(){
        return avgspeed;
    }

}
