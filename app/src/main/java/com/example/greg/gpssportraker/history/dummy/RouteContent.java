package com.example.greg.gpssportraker.history.dummy;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class RouteContent {

    public static List<RouteItem> ITEMS = new ArrayList<RouteItem>();

    public static Map<String, RouteItem> ITEM_MAP = new HashMap<String, RouteItem>();
/*
    static {
        // Add 3 sample items.
        addItem(new DummyItem("1", "2015.12.17.       13:01 \nDistance:           312 m \nDuration:           00:04:43"));
        addItem(new DummyItem("2", "2015.12.18.       17:41 \nDistance:           754 m \nDuration:           00:15:11"));
        addItem(new DummyItem("3", "2015.12.19.       11:34 \nDistance:           1955 m \nDuration:          01:04:31"));
    }
*/
    public static void addItem(RouteItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }


    public static class RouteItem {
        public String id;
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

        public RouteItem(String id,int vehicle, Date datum, float distance, double elevationgain,
                         ArrayList<LatLng> myroute, ArrayList<PolylineOptions> polylines,
                         ArrayList<Double> magassagok, ArrayList<Float> speeds, float avgspeed, int numberofelements, String duration) {
            this.id = id;
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
}
