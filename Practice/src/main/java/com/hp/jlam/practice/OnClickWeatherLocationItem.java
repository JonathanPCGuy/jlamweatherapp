package com.hp.jlam.practice;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

/**
 * Created by lamjon on 4/12/2014.
 */
public class OnClickWeatherLocationItem implements AdapterView.OnItemClickListener
{
    // this probably could be defined in-line the activity?


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Context context = view.getContext();
        // for now don't get the actual item; just fire up the
        // detailed activity
        Intent weatherIntent = new Intent(context, DetailedWeatherActivity.class);

        weatherIntent.putExtra(ExtraConstants.LOCATION_LAT,
                ((WeatherLocation)parent.getItemAtPosition(position)).getLocation_lat());

        weatherIntent.putExtra(ExtraConstants.LOCATION_LON,
                ((WeatherLocation)parent.getItemAtPosition(position)).getLocation_lon());

        weatherIntent.putExtra(ExtraConstants.LOCATION_COUNTRY,
                ((WeatherLocation)parent.getItemAtPosition(position)).getCountry());

        weatherIntent.putExtra(ExtraConstants.LOCATION_LOCATION,
                ((WeatherLocation)parent.getItemAtPosition(position)).getLocation());

        context.startActivity(weatherIntent);
        //startActivity(weatherIntent);
        return;
    }
}

