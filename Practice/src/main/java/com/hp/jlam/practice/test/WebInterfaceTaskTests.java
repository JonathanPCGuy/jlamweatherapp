package com.hp.jlam.practice.test;

import android.test.InstrumentationTestCase;
import com.hp.jlam.practice.weatherapi.WebInterfaceTask;

/**
 * Created by lamjon on 6/10/2014.
 */
public class WebInterfaceTaskTests extends InstrumentationTestCase
{


    public void testConstructLocationInfoURL() throws Exception
    {
        String actualString = WebInterfaceTask.ConstructFutureForecastURL(5.0,6.0,5);
        String expectedString = "http://api.openweathermap.org/data/2.5/forecast/daily?lat=5.00000&lon=6.00000&cnt=5&APPID=cbd7cd2ad1367e68fdfd3fe2009d3d6d";
        assertEquals(expectedString, actualString);
    }
}
