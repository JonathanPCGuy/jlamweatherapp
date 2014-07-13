package com.hp.jlam.practice.weatherapi;

import com.hp.jlam.practice.TempDisplayUnit;
import com.hp.jlam.practice.Utilities;

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

    //TempDisplayUnit
    public String GetHighDisplayString(TempDisplayUnit displayUnit)
    {
        return Utilities.GetFormattedTempString(this.high, displayUnit);
    }

    public String GetLowDisplayString(TempDisplayUnit displayUnit)
    {
        return Utilities.GetFormattedTempString(this.low, displayUnit);
    }
}
