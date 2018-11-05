package com.example.android.quakereport;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    public EarthquakeAdapter(Context context, List<Earthquake> earthquakes){
        super(context, 0, earthquakes);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_earthquake, parent, false);
        }
        Earthquake currentData = getItem(position);


        TextView magTextView = (TextView) listItemView.findViewById(R.id.magTextView);
        magTextView.setText(currentData.getMagnitude());


        TextView locationView = (TextView) listItemView.findViewById(R.id.locationTextView);
        locationView.setText(currentData.getLocation());

        //Create a new Data object from the time in milliseconds of the year
        Date dateObject = new Date(currentData.getTimeInMilliseconds());

        TextView dateView = (TextView) listItemView.findViewById(R.id.dateTextView);
        // Format the date string(i.e. "Mar 3, 1984")
        String formattedDate = formatDate(dateObject);
        // Display the date of the current earthquake in that TextView
        dateView.setText(formattedDate);

        TextView timeView = (TextView) listItemView.findViewById(R.id.timeTextView);
        // Format the time string (i.e. "4:30PM")
        String formattedTime = formatTime(dateObject);
        // Display the time of the current earthquake in that TextView
        timeView.setText(formattedTime);



        return listItemView;
    }

    private String formatDate(Date dateObject){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        return dateFormat.format(dateObject);
    }

    private String formatTime (Date dateObject){
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }
}
