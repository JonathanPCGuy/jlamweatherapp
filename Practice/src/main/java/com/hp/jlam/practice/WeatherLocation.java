package com.hp.jlam.practice;

/**
 * Created by lamjon on 1/30/14.
 */
public class WeatherLocation
{
    private String location;
    private int location_id;
    private String country;
    private String weather;
    private String weatherDescription;
    private String temperature;
    //timestamp?

    private boolean isSuccess;
    private String errorMessage;

    public long tag;
    public int id;

    public WeatherLocation()
    {
        this.isSuccess = true;
        this.InitStrings();
        this.errorMessage = new String();
        this.location_id = 0;
    }

    private void InitStrings()
    {
        this.location = new String();
        this.country = new String();
        this.weather = new String();
        this.weatherDescription = new String();
        this.temperature = new String();
    }

    public WeatherLocation(int id, String location, String country, int location_id)
    {
        this.isSuccess = true;
        this.InitStrings();
        this.location = location;
        this.country = country;
        this.location_id = location_id;
        this.id = id;
    }

    public WeatherLocation(String location, String country, int location_id)
    {
        this.InitStrings();
        this.location = location;
        this.country = country;
        this.location_id = location_id;
    }

    // probably should drop usage of isSuccess?
    public WeatherLocation(String errorMessage)
    {
        this.isSuccess = false;
        this.errorMessage = errorMessage;
        this.InitStrings();
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

    public boolean getIsSuccess()
    {
        return this.isSuccess;
    }

    public String getErrorMessage()
    {
        return this.errorMessage;
    }

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
