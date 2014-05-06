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
        Intent weatherIntent = new Intent(context, DetailedWeatherInfo.class);
        weatherIntent.putExtra(ExtraConstants.EXTRA_LOCATION_ID,
                ((WeatherLocation)parent.getItemAtPosition(position)).getLocation_id());

        weatherIntent.putExtra(ExtraConstants.EXTRA_LOCATION_COUNTRY,
                ((WeatherLocation)parent.getItemAtPosition(position)).getCountry());

        weatherIntent.putExtra(ExtraConstants.EXTRA_LOCATION_LOCATION,
                ((WeatherLocation)parent.getItemAtPosition(position)).getLocation());

        context.startActivity(weatherIntent);
        //startActivity(weatherIntent);
        return;
    }
}

