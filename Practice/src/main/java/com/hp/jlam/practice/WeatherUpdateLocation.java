package com.hp.jlam.practice;

/**
 * Created by lamjon on 6/23/2014.
 */
public class WeatherUpdateLocation
{
    public double lat = -9999;
    public double lon = -9999;
    public String locationFullName = "";
    /**
     * The location to use for the persistent weather updates.
     * @param lat
     * @param lon
     */
    public WeatherUpdateLocation(double lat, double lon, String locationFullName)
    {
        this.lat = lat;
        this.lon = lon;
        this.locationFullName = locationFullName;
    }

    public WeatherUpdateLocation()
    {

    }

}
