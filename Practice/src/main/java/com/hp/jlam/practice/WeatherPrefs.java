package com.hp.jlam.practice;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class WeatherPrefs
{

    private static SharedPreferences GetSharedPrefs(Context context)
    {
        return context.getSharedPreferences(AppConstants.PREFS_NAME, 0);
    }

    private static SharedPreferences GetDefaultSharedPrefs(Context context)
    {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    // this may move to a separate class later
    // for now leave it in here
    public static void SetWeatherUpdateLocation(Context context, WeatherUpdateLocation location)
    {
        String latString = Double.toString(location.lat);
        String lonString = Double.toString(location.lon);

        //String locationFullName = location.

        SharedPreferences settings = GetSharedPrefs(context);
        SharedPreferences.Editor editor = settings.edit();
        // because of the api not supporting double convert to string (fast and lazy way)
        editor.putString(ExtraConstants.LOCATION_LAT, latString);
        editor.putString(ExtraConstants.LOCATION_LON, lonString);
        editor.putString(ExtraConstants.LOCATION_FULL_NAME, location.locationFullName);
        editor.commit();
    }

    public static WeatherUpdateLocation GetWeatherUpdateLocation(Context context)
    {
        SharedPreferences settings = GetSharedPrefs(context);

        WeatherUpdateLocation location = new WeatherUpdateLocation();
        location.lat = Double.parseDouble(settings.getString(ExtraConstants.LOCATION_LAT, "-9999"));
        location.lon = Double.parseDouble(settings.getString(ExtraConstants.LOCATION_LON, "-9999"));
        location.locationFullName = settings.getString(ExtraConstants.LOCATION_FULL_NAME, "");
        return location;
    }

    public static int GetWeatherUpdateInterval(Context context)
    {
        SharedPreferences settings = GetDefaultSharedPrefs(context);
        return Integer.parseInt(settings.getString(context.getString(R.string.pref_key_weather_update_interval), "-1"));
    }

    public static TempDisplayUnit GetTempDisplayUnit(Context context)
    {
        SharedPreferences settings = GetDefaultSharedPrefs(context);
        String currentSetting = settings.getString(context.getString(R.string.pref_key_unit_measurement_temp),
                "K");

        if(currentSetting.equals("C"))
        {
            return TempDisplayUnit.CELSIUS;
        }
        else if (currentSetting.equals("F"))
        {
            return TempDisplayUnit.FAHRENHEIT;
        }
        else
        {
            return TempDisplayUnit.KELVIN;
        }
    }
}
