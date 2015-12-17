package com.example.greg.gpssportraker.history;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Geerg on 2015.12.17..
 */
public class HistoryContainer extends SugarRecord{

    private Date datum;
    public enum Vehicle {WALK,RUN,BICYCLE,AUTO}
    private Vehicle vehicle;
    private float distance;
    private double elevationgain;
    private ArrayList<LatLng> myroute = new ArrayList<>();
    private ArrayList<PolylineOptions> polylines = new ArrayList<PolylineOptions>();
    private ArrayList<Double> magassagok = new ArrayList<>();
    private ArrayList<Float> speeds = new ArrayList<>();
    private float avgspeed;
    private int arraylenght;

    public HistoryContainer(){

    }
    public HistoryContainer(int valami){

    }
}
