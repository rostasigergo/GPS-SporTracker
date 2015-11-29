package com.example.greg.gpssportraker.service;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.example.greg.gpssportraker.location.LDLocationManager;

/**
 * Created by Geerg on 2015.11.21..
 */
public class LocationService extends Service implements LocationListener {


    public static final String KEY_LOCATION = "KEY_LOCATION";
    public static final String BR_NEW_LOCATION = "BR_NEW_LOCATION";

    private boolean locationMonitorRunning = false;
    private LDLocationManager ldLocationManager = null;

    //firslocation?
    //lastlocation


   // public int onStartCommand(Intent intent, int flags, int startId){

    //}
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        //...
        if (!locationMonitorRunning) {
            locationMonitorRunning = true;
            ldLocationManager = new LDLocationManager(getApplicationContext(),this);
            ldLocationManager.startLocationMinitoring();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if (ldLocationManager != null){
            ldLocationManager.startLocationMinitoring();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        //First, lastlocaiton kezelés

        Intent locchangedintent = new Intent(BR_NEW_LOCATION);
        locchangedintent.putExtra(KEY_LOCATION,location);
        LocalBroadcastManager.getInstance(this).sendBroadcast(locchangedintent);
        Toast.makeText(this, "Wow, a new point", Toast.LENGTH_SHORT).show();//Toastot majd törölni importból!!!
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
