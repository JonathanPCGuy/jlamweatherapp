package com.hp.jlam.practice.ui;

/**
 * Created by lamjon on 1/30/14.
 */
public class WeatherLocation
{
    private String location;
    private int location_id;

    private double location_lat;
    private double location_lon;

    private String country;
    private String weather;
    private String weatherDescription;
    private String temperature;
    //timestamp?

    public int id;

    public WeatherLocation()
    {
        this.InitStrings();
        this.location_id = 0;

        this.location_lat = 0;
        this.location_lon = 0;
    }

    public WeatherLocation(int id, String location, String country, double location_lat, double location_lon)
    {
        this.InitStrings();
        this.location = location;
        this.country = country;

        this.location_lat = location_lat;
        this.location_lon = location_lon;

        this.id = id;
    }

    private void InitStrings()
    {
        this.location = new String();
        this.country = new String();
        this.weather = new String();
        this.weatherDescription = new String();
        this.temperature = new String();
    }



    public String getFullLocation()
    {
        if(country.length() == 0)
        {
            return this.location;
        }
        else if (location.length() != 0)
        {
            return this.location + ", " + this.country;
        }
        else
        {
            return this.country;
        }

    }

    public void setId(int id) {this.id = id;}

    public int getId() {return this.id;}

    public void setLocation_id(int id) {this.location_id = id;}

    public int getLocation_id() {return this.location_id;}

    public void setLocation_lat(double location_lat) {this.location_lat = location_lat;}

    public double getLocation_lat() { return  this.location_lat;}

    public void setLocation_lon(double location_lon) {this.location_lon = location_lon;}

    public double getLocation_lon(){return  this.location_lon;}

    public void setLocation(String location)
    {
        this.location = location;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public void setWeather(String weather)
    {
        this.weather = weather;
    }

    public  void setWeatherDescription(String weatherDescription)
    {
        this.weatherDescription = weatherDescription;
    }

    public void setTemperature(String temperature)
    {
        this.temperature = temperature;
    }

    public String getLocation()
    {
        return this.location;
    }

    public String getCountry()
    {
        return this.country;
    }

    public String getWeather()
    {
        return this.weather;
    }

    public String getTemperature()
    {
        return this.temperature;
    }

    public String getWeatherDescription() {return this.weatherDescription;}

}
