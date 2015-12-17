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
import com.example.greg.gpssportraker.dialog.HistoryDialog;
import com.example.greg.gpssportraker.history.HistoryListActivity;
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

    private GoogleMap mMap;

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


        lcont = new LocationContainer();

        StartWalkBtn = (Button) findViewById(R.id.StartWalk);
        StartWalkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartButtonClick(stepWalk, StartWalkBtn);
            }
        });
        StartRunBtn = (Button) findViewById(R.id.StartRun);
        StartRunBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartButtonClick(stepRun, StartRunBtn);
            }
        });
        StartBicycleBtn = (Button) findViewById(R.id.StartBicycle);
        StartBicycleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartButtonClick(stepBicycle, StartBicycleBtn);
            }
        });
        StartAutoBtn = (Button) findViewById(R.id.StartAuto);
        StartAutoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartButtonClick(stepAuto, StartAutoBtn);
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
                if (pref.getBoolean(MONITORINGISON, false)) {
                    HistoryDialog historydialog = new HistoryDialog();
                    historydialog.show(getFragmentManager(),"History Dialog");
                    //StopButtonClick();//?
                }
                else {
                    Intent showHistory = new Intent(getApplicationContext(), HistoryListActivity.class);
                    startActivity(showHistory);
                }
            }
        });

        duration = (TextView) findViewById(R.id.TVDuration);
        distance = (TextView) findViewById(R.id.TVdistance);
        currvelo = (TextView) findViewById(R.id.TVcurrentvelo);
        elevationgain = (TextView) findViewById(R.id.TVelevationgain);
        avrvelo = (TextView) findViewById(R.id.TVaveragevelo);

        if (pref.getBoolean(MONITORINGISON, false)){
            setStopButtonsVisible();
        }
        else {
            setStartButtonsVisible();
        }

        if (pref.getBoolean(PAUSEISON, false)) {
            PauseBtn.setText(R.string.resume);

        }
        else {
            PauseBtn.setText(R.string.pause);
        }


        elapsedTime = pref.getInt(ELAPSEDTIME,0);
        timer = new CountDownTimer(172800000,1000) {//2nap..
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

    private void StartButtonClick(float mindist, Button pressedbtn){
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if(!provider.contains("gps")){ //GPS kikapcsolva
            //Animation Wrong!
            GPSDialog gpsdialog = new GPSDialog();
            gpsdialog.show(getFragmentManager(),"GPS Dialog");
        }
        else {
            Animation showAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.pushanim);
            pressedbtn.startAnimation(showAnim);
            //várakozás első jelre
            Intent i = new Intent(getApplicationContext(), LocationService.class);
            i.putExtra("minDist", mindist);

            editor.putFloat(CURRENTMINDIST, mindist);
            editor.putBoolean(MONITORINGISON, true);
            editor.putBoolean(PAUSEISON, false);
            elapsedTime = 0;
            editor.putInt(ELAPSEDTIME,elapsedTime);
            editor.commit();
            timer.start();

            PauseBtn.setText(R.string.pause);
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

        if (pref.getBoolean(PAUSEISON, false)) {
            editor.putBoolean(PAUSEISON, false);
            i.putExtra("minDist", pref.getFloat(CURRENTMINDIST, 10));
            PauseBtn.setText(R.string.pause);
            timer.start();
            startService(i);

        } else {
            editor.putBoolean(PAUSEISON, true);
            PauseBtn.setText(R.string.resume);
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


    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            if (mMap != null) {
                //setUpMap();
            }
        }
    }

    CameraUpdate update;
    private void setUpMap() {

        //Kamera Frissítés!
        update = CameraUpdateFactory.newLatLngZoom(currentlatlng,16);
        mMap.animateCamera(update);

        if (lcont != null) {
            mMap.addPolyline(lcont.getPolyline());
            currvelo.setText(speedformat(lcont.getLastSpeed()));//(Float.toString(lcont.getLastSpeed())+ " m/s");
            distance.setText(distanceFormat(lcont.getDistance()));
            avrvelo.setText(speedformat(lcont.getAvgspeed()));//(Float.toString(lcont.getAvgspeed())+ " m/s");
            elevationgain.setText(Double.toString(lcont.getElevationgain()) + " m");
        }


    }

    LatLng currentlatlng;
    double currentaltitude;
    float currentspeed;
    float currentAccu;
    private BroadcastReceiver NewLocMsg = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Location newLocation = intent.getParcelableExtra(LocationService.KEY_LOCATION);

            currentlatlng = new LatLng(newLocation.getLatitude(),newLocation.getLongitude());
            currentaltitude = newLocation.getAltitude();
            currentspeed = newLocation.getSpeed();
            currentAccu = newLocation.getAccuracy();

            //Toast.makeText(getApplicationContext(),Float.toString(currentAccu),Toast.LENGTH_LONG).show();

            //if (currentAccu <= 10f) {
                if (lcont != null) {
                    lcont.addNewLocation(currentlatlng, currentaltitude, currentspeed, currentAccu);
                }
            //}
            setUpMap();

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
    private String distanceFormat(float distance){
        //String dist = Float.toString(distance) + " m";
        String dist;
        if (distance < 10000) {
            dist = String.format("%.0f", distance) + " [m]";
        }
        else{
            dist = String.format("%.2f", distance/1000) + " [km]";
        }
        return dist;
    }
    private String speedformat(float speed){
        String sebesseg = String.format("%.1f",speed*3.6) + " [km/h]";

        return sebesseg;
    }
}
