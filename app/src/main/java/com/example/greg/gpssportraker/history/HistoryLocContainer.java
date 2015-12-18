package com.example.greg.gpssportraker.history;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Geerg on 2015.12.17..
 */
public class HistoryLocContainer extends SugarRecord{

    private Date datum;
    private int vehicle;
    private float distance;
    private double elevationgain;
    private ArrayList<LatLng> myroute = new ArrayList<>();
    private ArrayList<PolylineOptions> polylines = new ArrayList<PolylineOptions>();
    private ArrayList<Double> magassagok = new ArrayList<>();
    private ArrayList<Float> speeds = new ArrayList<>();
    private float avgspeed;
    private int numberofelements;//Polyline esetében -1 !!!
    private String duration;

    public HistoryLocContainer(){
        vehicle = 0;
        distance = 0f;
        elevationgain = 0.0;
        avgspeed = 0f;
        numberofelements = 0;
        duration = "00:00:00";
    }
    public HistoryLocContainer(int vehicle, Date datum, float distance, double elevationgain,
                               ArrayList<LatLng> myroute, ArrayList<PolylineOptions> polylines,
                               ArrayList<Double> magassagok, ArrayList<Float> speeds, float avgspeed, int numberofelements, String duration){
        this.vehicle = vehicle;
        this.datum = datum;
        this.distance = distance;
        this.elevationgain = elevationgain;
        this.myroute = myroute;
        this.polylines = polylines;
        this.magassagok = magassagok;
        this.speeds = speeds;
        this.avgspeed = avgspeed;
        this.numberofelements = numberofelements;
        this.duration = duration;
    }

    public Date getDatum(){
        return datum;
    }
    public int getVehicle(){
        return vehicle;
    }
    public float getDistance(){
        return distance;
    }
    public double getElevationGain(){
        return elevationgain;
    }
    public int getNumberOfElements(){
        return numberofelements;
    }
    public float getAvgSpeed(){
        return avgspeed;
    }
    public ArrayList<LatLng> getMyroute(){
        return myroute;
    }
    public ArrayList<PolylineOptions> getPolylines(){
        return polylines;
    }
    public ArrayList<Double> getMagassagok(){
        return magassagok;
    }
    public ArrayList<Float> getSpeeds(){
        return speeds;
    }
    public String getDuration(){return duration;}
}
