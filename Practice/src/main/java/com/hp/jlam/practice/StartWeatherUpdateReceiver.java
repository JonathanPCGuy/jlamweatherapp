package com.hp.jlam.practice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by lamjon on 6/22/2014.
 */
public class StartWeatherUpdateReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.d("StartWeatherUpdateReceiver", "Broadcast received!");
        Intent serviceIntent = new Intent(context, WeatherUpdateIntentService.class);
        // later on we'll need to actually pull in the values the user sets
        serviceIntent.putExtra(ExtraConstants.LOCATION_LAT, 51.50853);
        serviceIntent.putExtra(ExtraConstants.LOCATION_LON, -0.12574);
        context.startService(serviceIntent);
        // could i combine the two somehow? or better to keep them separate
    }
}
