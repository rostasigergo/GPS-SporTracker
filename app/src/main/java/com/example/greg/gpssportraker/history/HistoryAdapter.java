package com.example.greg.gpssportraker.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.greg.gpssportraker.R;
import com.example.greg.gpssportraker.history.dummy.RouteContent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Geerg on 2015.12.17..
 */
public class HistoryAdapter extends BaseAdapter {

    private final ArrayList<RouteContent.RouteItem> hcontainer;

    public HistoryAdapter(final Context context, final ArrayList<RouteContent.RouteItem> ahcontainer){
        hcontainer = ahcontainer;
    }

    @Override
    public int getCount() {
        return hcontainer.size();
    }

    @Override
    public Object getItem(int position) {
        return hcontainer.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final RouteContent.RouteItem cc = hcontainer.get(position);

        View itemView = convertView;

        if (itemView == null){
            LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(R.layout.historyrow, null);
        }

        ImageView Icon = (ImageView) itemView.findViewById(R.id.imageView);
        switch(cc.getVehicle()){
            case 1:
                Icon.setImageResource(R.drawable.walking);
                break;
            case 2:
                Icon.setImageResource(R.drawable.running);
                break;
            case 3:
                Icon.setImageResource(R.drawable.bicycle);
                break;
            case 4:
                Icon.setImageResource(R.drawable.car);
                break;
            default:
                Icon.setImageResource(R.drawable.walking);
                break;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("E yyyy.MM.dd hh:mm:ss");
        TextView title = (TextView)itemView.findViewById(R.id.tvTitle);
        title.setText(sdf.format(cc.getDatum()));

        TextView datas = (TextView)itemView.findViewById(R.id.tvDatas);
        datas.setText(cc.getDuration() + "         " + distanceFormat(cc.getDistance()));

        return itemView;
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
}
