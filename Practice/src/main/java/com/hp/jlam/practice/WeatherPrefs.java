package com.hp.jlam.practice;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by lamjon on 6/23/2014.
 */
public class WeatherPrefs
{

    // this may move to a separate class later
    // for now leave it in here
    public static void SetWeatherUpdateLocation(Context context, WeatherUpdateLocation location)
    {
        String latString = Double.toString(location.lat);
        String lonString = Double.toString(location.lon);

        //String locationFullName = location.

        SharedPreferences settings = context.getSharedPreferences(AppConstants.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        // because of the api not supporting double convert to string (fast and lazy way)
        editor.putString(ExtraConstants.LOCATION_LAT, latString);
        editor.putString(ExtraConstants.LOCATION_LON, lonString);
        editor.putString(ExtraConstants.LOCATION_FULL_NAME, location.locationFullName);
        editor.commit();
    }

    public static WeatherUpdateLocation GetWeatherUpdateLocation(Context context)
    {
        SharedPreferences settings = context.getSharedPreferences(AppConstants.PREFS_NAME, 0);

        WeatherUpdateLocation location = new WeatherUpdateLocation();
        location.lat = Double.parseDouble(settings.getString(ExtraConstants.LOCATION_LAT, "-9999"));
        location.lon = Double.parseDouble(settings.getString(ExtraConstants.LOCATION_LON, "-9999"));
        location.locationFullName = settings.getString(ExtraConstants.LOCATION_FULL_NAME, "");
        return location;
    }


}
