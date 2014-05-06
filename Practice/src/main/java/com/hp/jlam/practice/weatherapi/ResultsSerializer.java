package com.hp.jlam.practice.weatherapi;

// probably should group all of the weather api stuff into a namespace (package?)

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by lamjon on 4/22/2014.
 */
public class ResultsSerializer
{
    public static FutureDailyForecast ProcessFutureDailyForecastJSON(String jsonString) throws JSONException, IllegalArgumentException

    {
        JSONObject jsonObject = new JSONObject(jsonString);

        // get out number of days? (necessary?) maybe for validation

        int expectedNumOfDays = jsonObject.getInt("cnt");

        JSONArray jsonArray = jsonObject.getJSONArray("list");

        if (jsonArray.length() != expectedNumOfDays)
        {
            throw new IllegalArgumentException("Forecast length (days) does not match array length.");
        }

        FutureDailyForecast futureDailyForecast = new FutureDailyForecast();

        for(int i = 0; i < jsonArray.length(); i++)
        {
            DailyForecast dailyForecast = new DailyForecast();
            dailyForecast.high = jsonArray.getJSONObject(i).getJSONObject("temp").getDouble("max");
            dailyForecast.low = jsonArray.getJSONObject(i).getJSONObject("temp").getDouble("min");

            dailyForecast.date = new Date(Long.parseLong(jsonArray.getJSONObject(i).getString("dt")) * 1000);
            futureDailyForecast.futureForecast.add(dailyForecast);
        }

        return  futureDailyForecast;
    }

}
