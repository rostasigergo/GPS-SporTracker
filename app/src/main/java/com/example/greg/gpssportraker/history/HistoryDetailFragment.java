package com.example.greg.gpssportraker.history;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.greg.gpssportraker.R;
import com.example.greg.gpssportraker.history.dummy.RouteContent;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
//import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


public class HistoryDetailFragment extends Fragment {


    private GoogleMap fMap;
    //private MapView mapView;

    public static final String ARG_ITEM_ID = "item_id";

    private RouteContent.RouteItem mItem;

    public HistoryDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mItem = RouteContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
        }

        //if (fMap == null) {
            //fMap = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.fmap)).getMap();
           // fMap = ((SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.fmap)).getMap();

        //}
        /*if (fMap != null){
            fMap.clear();
        }*/
    }

    CameraUpdate CAMupdate;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            if (fMap == null) {
                //fMap = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.fmap)).getMap();
            fMap = ((SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.fmap)).getMap();
            }

            ((TextView) rootView.findViewById(R.id.hTVDuration)).setText(mItem.getDuration());
            ((TextView) rootView.findViewById(R.id.hTVdistance)).setText(distanceFormat(mItem.getDistance()));
            ((TextView) rootView.findViewById(R.id.hTVelevationgain)).setText(Double.toString(mItem.getElevationGain())+ " [m]");
            ((TextView) rootView.findViewById(R.id.hTVaveragevelo)).setText(speedformat(mItem.getAvgSpeed()));
            if (fMap != null){
                fMap.clear();
                for (int i = 0; i < mItem.getPolylines().size(); i++){
                    fMap.addPolyline(mItem.getPolylines().get(i));
                }
                Toast.makeText(getContext(),Integer.toString(mItem.getPolylines().size()), Toast.LENGTH_LONG).show();

                //if (mItem.getMyroute() != null) {
                    //CAMupdate = CameraUpdateFactory.newLatLngZoom(mItem.getMyroute().get(0),16);
                    //fMap.animateCamera(CAMupdate);
               // }
            }
        }


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
