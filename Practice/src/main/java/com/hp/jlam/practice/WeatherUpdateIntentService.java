package com.hp.jlam.practice;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.hp.jlam.practice.receivers.UpdateWeatherNotificationReceiver;
import com.hp.jlam.practice.ui.WeatherLocation;
import com.hp.jlam.practice.weatherapi.ResultsSerializer;
import com.hp.jlam.practice.weatherapi.WebInterfaceTask;

/**
 * Created by lamjon on 6/17/2014.
 */


public class WeatherUpdateIntentService extends IntentService
{

    /*
        1. can this pick up on the setting set
            - we can pull the data from settings
            - but it would be a pointer to the instance selected

            how would we link it to the same item (i.e. as if you clicked on an item in the app)
            and not have to pass in the full object data (not the same object)

            or maybe it doesn't matter

            for now this is outside the scope of the user story. get the alarm and intent service
            working first, then we'll worry about the pref stuff later

     */
    public WeatherUpdateIntentService()
    {
        super("weather-update-service");
    }

    private static final int NOTIFICATION_ID = R.string.local_service_started;

    @Override
    protected void onHandleIntent(Intent intent)
    {
        Log.d("WeatherUpdateIntentService", "in onHandleIntent");
        // get target location info
        // do web api query
        // if success update notification
        double lat = intent.getDoubleExtra(ExtraConstants.LOCATION_LAT, -9999);
        double lon = intent.getDoubleExtra(ExtraConstants.LOCATION_LON, -9999);
        
        //todo: if value is -9999 abort intent?

        WebInterfaceTask webInterfaceTask = WebInterfaceTask.CreateWeatherLocationTask(lat, lon);

        String errorMsg = "";
        boolean success = false;
        WeatherLocation currentWeather = null;

        try
        {
            String result = webInterfaceTask.execute().get();

            if (webInterfaceTask.get_isSuccess())
            {
                // parse the JSON and update the notification
                currentWeather = ResultsSerializer.ParseCurrentWeather(result);

                success = true;
            }
        }
        catch(Exception e)
        {
            errorMsg = e.getMessage();
        }

        if(errorMsg.length() > 0)
        {
            Log.e("WeatherUpdateIntentService", errorMsg);
        }

        Intent resultBroadcast = new Intent(this, UpdateWeatherNotificationReceiver.class);
        //new Intent(IntentConstants.WEATHER_UPDATE);

        resultBroadcast.putExtra(ExtraConstants.UPDATE_SUCCESS, success);
        if(success)
        {
            resultBroadcast.putExtra(ExtraConstants.LOCATION_LOCATION, currentWeather.getLocation());
            resultBroadcast.putExtra(ExtraConstants.LOCATION_COUNTRY, currentWeather.getCountry());
            // this needs to be converted to C/F
            resultBroadcast.putExtra(ExtraConstants.LOCATION_CURRENT_TEMP, Double.parseDouble(currentWeather.getTemperature()));
        }
        // broadcast!
        Log.d("WeatherUpdateIntentService", "About to send broadcast");
        sendBroadcast(resultBroadcast);
    }
}
