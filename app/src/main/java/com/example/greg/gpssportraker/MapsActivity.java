package com.example.greg.gpssportraker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.greg.gpssportraker.location.LocationContainer;
import com.example.greg.gpssportraker.service.LocationService;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private float inkrement = 0;
    private float ink2 = 0;
    private boolean monitoringison = false;//átmeneti!!!
    private PolylineOptions vonaloptions;
    private PolylineOptions vonaloptions2;

    private LocationContainer lcont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();

        final Button StartStopBtn = (Button) findViewById(R.id.StartStop);
        StartStopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                Intent i = new Intent(getApplicationContext(), LocationService.class);
                if (monitoringison) {
                    StartStopBtn.setText("Start");
                    monitoringison = false;
                    stopService(i);
                } else {
                    StartStopBtn.setText("Stop");
                    monitoringison = true;
                    startService(i);
                }
            }
        });
        vonaloptions = new PolylineOptions();
        vonaloptions.color(Color.RED);
        vonaloptions.width(5);

        vonaloptions2 = new PolylineOptions();
        vonaloptions2.color(Color.BLUE);
        vonaloptions2.width(5);
        lcont = new LocationContainer();
        Button PauseBtn = (Button) findViewById(R.id.Pause);
        PauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ink2 = ink2*-1;
                if (ink2 > 0){
                    ink2 = ink2+1;
                }
                else{
                    ink2 = ink2-1;
                }
                vonaloptions.add(new LatLng(ink2, inkrement));
                //vonaloptions.add()
                mMap.addPolyline(vonaloptions);
                inkrement = inkrement+1;
                /*mMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(0,0), new LatLng(0, inkrement))
                        .width(5)
                        .color(Color.RED));*/

                //if (lcont.getPoints()!= null) {
                  //  mMap.addMarker(new MarkerOptions().position(lcont.getPoint()).title("Itt vagyok"));
                //}
                Toast.makeText(MapsActivity.this, "Inkrement: " + inkrement, Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        LocalBroadcastManager.getInstance(getApplication()).registerReceiver(
                NewLocMsg,
                new IntentFilter(LocationService.BR_NEW_LOCATION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getApplication()).unregisterReceiver(
                NewLocMsg
        );
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    //Polyline line;
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));

        //line = mMap.addPolyline(new PolylineOptions()
        //        .add(new LatLng(51.5, -0.1), new LatLng(40.7, -74.0),new LatLng(0,0))
        //        .width(5)
        //        .color(Color.RED));//addAll betöltéshez


    }


    CameraUpdate update;
    //Új pozíció érkezett! felrajzolni...
    private BroadcastReceiver NewLocMsg = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Toast.makeText(getApplicationContext(),"BROADDCAST",Toast.LENGTH_LONG).show();
            Location newLocation = intent.getParcelableExtra(LocationService.KEY_LOCATION);
            //myroute.add(new LatLng(newLocation.getLatitude(),newLocation.getLongitude()));
            lcont.addPoint(new LatLng(newLocation.getLatitude(), newLocation.getLongitude()));

            mMap.addMarker(new MarkerOptions().position(new LatLng(newLocation.getLatitude(), newLocation.getLongitude())).title("Marker"));

            //new LatLng(newLocation.getLatitude(), newLocation.getLongitude()   külön VÁLTOZÓBA!!!!!!!!!!!!!!!!!!!!!!!!
            vonaloptions2.add(new LatLng(newLocation.getLatitude(), newLocation.getLongitude()));
            mMap.addPolyline(vonaloptions2);
            update = CameraUpdateFactory.newLatLngZoom(new LatLng(newLocation.getLatitude(), newLocation.getLongitude()),16);//16szoros mennyi lesz?
            Toast.makeText(getApplicationContext(),"BROADDCAST",Toast.LENGTH_LONG).show();
            mMap.animateCamera(update);
            //currentLocation.getLatitude();
            //currentLocation.getLongitude();
            //currentLocation.getAltitude();
            //currentLocation.getSpeed();
            //currentLocation.getTime();  ha kellene....

        }
    };
}
