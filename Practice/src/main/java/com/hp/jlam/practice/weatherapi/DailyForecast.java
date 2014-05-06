package com.hp.jlam.practice.weatherapi;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by lamjon on 4/22/2014.
 */
public class DailyForecast
{
    public double high;
    public double low;
    public Date date;
    // for now that's all we'll do, will need to insert more info later

    public static long ConvertToF(double temp)
    {
        return Math.round((ConvertToC(temp)) * 1.8 + 32);
    }

    public static double ConvertToC(double temp)
    {
        return (temp) - 273.15;
    }
}
