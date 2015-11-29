package com.example.greg.gpssportraker.location;

import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;

/**
 * Created by Geerg on 2015.11.21..
 */
public class LDLocationManager {

    private Context context;
    private LocationListener loclistener;
    private LocationManager locManager;

    public LDLocationManager(Context aContext, LocationListener loclistener){
        context = aContext;
        this.loclistener = loclistener;
        locManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public void startLocationMinitoring(){
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,100,100,loclistener);//legalább 10 m pontos teszteléshez 50...

    }
    public void stopLocationMonitoring(){
        if (locManager != null){
           //locManager.removeUpdates(loclistener);
            locManager.removeUpdates(loclistener);
        }
    }
}
