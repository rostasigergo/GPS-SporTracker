package com.example.greg.gpssportraker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.greg.gpssportraker.dialog.GPSDialog;
import com.example.greg.gpssportraker.location.LocationContainer;
import com.example.greg.gpssportraker.service.LocationService;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.NumberFormat;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private PolylineOptions vonaloptions2;

    private boolean monitoringison;
    private boolean pauseison;
    private int elapsedTime;

    private LocationContainer lcont;

    //SharedPreferences
    private static final String MYPREF = "My_Pref";
    private static final String MONITORINGISON = "LOC_MON_SER_IS_ON";
    private static final String PAUSEISON = "LOC_MON_SER_IS_PAUSE";
    private static final String CURRENTMINDIST = "CURRENT_STEP";
    private static final String ELAPSEDTIME = "ELAPSED_TIME";
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private final static float stepWalk = 8;
    private final static float stepRun = 10;
    private final static float stepBicycle = 15;
    private final static float stepAuto = 25;

    Button StartWalkBtn;
    Button StartRunBtn;
    Button StartBicycleBtn;
    Button StartAutoBtn;
    Button PauseBtn;
    Button StopBtn;
    Button HistoryBtn;

    TextView duration;
    TextView distance;
    TextView currvelo;
    TextView avrvelo;
    TextView elevationgain;

    CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();

        pref = getSharedPreferences(MYPREF, MODE_PRIVATE);
        editor = pref.edit();



        StartWalkBtn = (Button) findViewById(R.id.StartWalk);
        StartWalkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartButtonClick(stepWalk);
            }
        });
        StartRunBtn = (Button) findViewById(R.id.StartRun);
        StartRunBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartButtonClick(stepRun);
            }
        });
        StartBicycleBtn = (Button) findViewById(R.id.StartBicycle);
        StartBicycleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartButtonClick(stepBicycle);
            }
        });
        StartAutoBtn = (Button) findViewById(R.id.StartAuto);
        StartAutoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartButtonClick(stepAuto);
            }
        });
        StopBtn = (Button) findViewById(R.id.Stop);
        StopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StopButtonClick();
            }
        });
        PauseBtn = (Button) findViewById(R.id.Pause);
        PauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PauseButtonClick();
            }
        });
        HistoryBtn = (Button) findViewById(R.id.HistoryBtn);
        HistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //New Itent stb
            }
        });

        duration = (TextView) findViewById(R.id.TVDuration);
        distance = (TextView) findViewById(R.id.TVdistance);
        currvelo = (TextView) findViewById(R.id.TVcurrentvelo);
        elevationgain = (TextView) findViewById(R.id.TVelevationgain);
        avrvelo = (TextView) findViewById(R.id.TVaveragevelo);

        monitoringison = pref.getBoolean(MONITORINGISON, false);
        pauseison = pref.getBoolean(PAUSEISON, false);
        if (monitoringison){
            setStopButtonsVisible();
        }
        else {
            setStartButtonsVisible();
        }

        if (pauseison) {
            PauseBtn.setText("Resume");
        }
        else {
            PauseBtn.setText("Pause");
        }

        vonaloptions2 = new PolylineOptions();
        vonaloptions2.color(Color.RED);
        vonaloptions2.width(10);
        lcont = new LocationContainer();

        elapsedTime = pref.getInt(ELAPSEDTIME,0);
        //2 nap korlát!!!
        timer = new CountDownTimer(172800000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                elapsedTime++;
                duration.setText(secToTimeFormat(elapsedTime));
            }

            @Override
            public void onFinish() {

            }
        };

    }

    private void StartButtonClick(float mindist){
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if(!provider.contains("gps")){ //GPS kikapcsolva
            GPSDialog gpsdialog = new GPSDialog();
            gpsdialog.show(getFragmentManager(),"GPS Dialog");
        }
        else {
            Intent i = new Intent(getApplicationContext(), LocationService.class);
            i.putExtra("minDist", mindist);
            editor.putFloat(CURRENTMINDIST, mindist);
            editor.putBoolean(MONITORINGISON, true);
            editor.putBoolean(PAUSEISON, false);
            elapsedTime = 0;
            editor.putInt(ELAPSEDTIME,elapsedTime);
            editor.commit();
            timer.start();
            PauseBtn.setText("Pause");
            setStopButtonsVisible();
            startService(i);
        }
    }
    private void StopButtonClick(){
        Intent i = new Intent(getApplicationContext(), LocationService.class);
        editor.putBoolean(MONITORINGISON, false);
        editor.putBoolean(PAUSEISON, false);
        timer.cancel();
        editor.commit();
        setStartButtonsVisible();
        /*
            MENTÉS!!!
        */
        stopService(i);
    }

    private void PauseButtonClick(){

        Intent i = new Intent(getApplicationContext(), LocationService.class);
        Animation showAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.pushanim);

        PauseBtn.startAnimation(showAnim);

        pauseison = pref.getBoolean(PAUSEISON, false);
        if (pauseison) {
            editor.putBoolean(PAUSEISON, false);
            i.putExtra("minDist", pref.getFloat(CURRENTMINDIST, 10));
            PauseBtn.setText("Pause");
            timer.start();
            startService(i);

        } else {
            editor.putBoolean(PAUSEISON, true);
            PauseBtn.setText("Resume");
            timer.cancel();
            editor.putInt(ELAPSEDTIME, elapsedTime);
            stopService(i);
        }
        editor.commit();

    }
    private void setStartButtonsVisible(){
        StartWalkBtn.setVisibility(View.VISIBLE);
        StartRunBtn.setVisibility(View.VISIBLE);
        StartBicycleBtn.setVisibility(View.VISIBLE);
        StartAutoBtn.setVisibility(View.VISIBLE);
        StopBtn.setVisibility(View.GONE);
        PauseBtn.setVisibility(View.GONE);
    }
    private void setStopButtonsVisible(){
        StartWalkBtn.setVisibility(View.GONE);
        StartRunBtn.setVisibility(View.GONE);
        StartBicycleBtn.setVisibility(View.GONE);
        StartAutoBtn.setVisibility(View.GONE);
        StopBtn.setVisibility(View.VISIBLE);
        PauseBtn.setVisibility(View.VISIBLE);
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
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        LocalBroadcastManager.getInstance(getApplication()).unregisterReceiver(
                NewLocMsg
        );
        editor.putInt(ELAPSEDTIME, elapsedTime);
        editor.commit();

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
    private void setUpMap() {
        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));

        //line = mMap.addPolyline(new PolylineOptions()
        //        .add(new LatLng(51.5, -0.1), new LatLng(40.7, -74.0),new LatLng(0,0))
        //        .width(5)
        //        .color(Color.RED));//addAll betöltéshez


    }


    int step = 0;
    CameraUpdate update;
    private BroadcastReceiver NewLocMsg = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Location newLocation = intent.getParcelableExtra(LocationService.KEY_LOCATION);
            //myroute.add(new LatLng(newLocation.getLatitude(),newLocation.getLongitude()));
            lcont.addPoint(new LatLng(newLocation.getLatitude(), newLocation.getLongitude()));

            //new LatLng(newLocation.getLatitude(), newLocation.getLongitude()   külön VÁLTOZÓBA!!!!!!

            vonaloptions2.add(new LatLng(newLocation.getLatitude(), newLocation.getLongitude()));
            float speed = newLocation.getSpeed();//m/s -ba
            /* int sp = (int)speed*10;
            if (sp > 255) sp = 255;
            vonaloptions2.color(Color.rgb(sp,sp,sp));*/

            step++;
            if (step == 1){
                vonaloptions2.color(Color.GREEN);
            }
            if (step == 2){
                vonaloptions2.color(Color.WHITE);
            }
            if (step == 3){
                vonaloptions2.color(Color.BLUE);
            }
            if (step > 3){
                vonaloptions2.color(Color.RED);
            }

            mMap.addPolyline(vonaloptions2);
            //currentLocation.getLatitude();
            //currentLocation.getLongitude();
            //currentLocation.getAltitude();
            //currentLocation.getSpeed();
            //currentLocation.getTime();  ha kellene....

            mMap.addMarker(new MarkerOptions().position(new LatLng(newLocation.getLatitude(), newLocation.getLongitude())).title("Marker + " + speed));
            update = CameraUpdateFactory.newLatLngZoom(new LatLng(newLocation.getLatitude(), newLocation.getLongitude()),16);
            mMap.animateCamera(update);
            Toast.makeText(getApplicationContext(), "Wow, a new point received", Toast.LENGTH_LONG).show();

        }
    };

    NumberFormat myFormat;
    private String secToTimeFormat(int seconds){
        myFormat = NumberFormat.getInstance();
        myFormat.setMinimumIntegerDigits(2);
        String ido;
        int hours = (int) seconds / 3600;
        int maradek = (int) seconds - hours * 3600;
        int mins = maradek / 60;
        maradek = maradek - mins * 60;
        int secs = maradek;
        ido = myFormat.format(hours) + ":" + myFormat.format(mins) + ":" + myFormat.format(secs);
        return ido;
    }
}
