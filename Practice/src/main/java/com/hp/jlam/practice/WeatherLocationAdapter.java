package com.hp.jlam.practice;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by lamjon on 2/14/14.
 */

public class WeatherLocationAdapter extends ArrayAdapter<WeatherLocation>
{
    // need to use weatherresult so i can hold temps and later weather icons
    private Context context;
    private ArrayList<WeatherLocation> objects = null;
    private int layoutResourceId;

    public WeatherLocationAdapter(Context context, int layoutResourceId, ArrayList<WeatherLocation> objects)
    {
        super(context, layoutResourceId, objects);
        this.objects = objects;
        this.layoutResourceId = layoutResourceId;
        this.context = context;
    }

    //todo: how to support real-time update of data?
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // see http://www.javacodegeeks.com/2013/09/android-listview-with-adapter-example.html
        // for reason for doing this
        if(convertView == null)
        {
            // inflate the layout
            LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
            convertView = layoutInflater.inflate(layoutResourceId, parent, false);
        }

        // object item based on position
        WeatherLocation weatherResult = objects.get(position);

        // from the layout of the item get the views and put in the data

        TextView textViewLocation = (TextView)convertView.findViewById(R.id.textViewLocation);
        textViewLocation.setText(weatherResult.getLocation());

        TextView textViewCountry = (TextView)convertView.findViewById(R.id.textFutureForecastCountry);
        textViewCountry.setText(weatherResult.getCountry());

        // what is settag? idenitifer used to find item when it is clicked on; implement later
        return convertView;
    }

    @Override
    public WeatherLocation getItem(int position)
    {
        return objects.get(position);
    }


    public void UpdateValues(ArrayList<WeatherLocation> objects)
    {
        this.objects = objects;
    }


}
