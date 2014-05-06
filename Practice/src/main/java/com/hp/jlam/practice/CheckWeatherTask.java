package com.hp.jlam.practice;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by lamjon on 1/21/14.
 */

// howto rename?
// template class?

// for now i'm just going to stick this inside the activity
// will make it separate later
    // what does this template mean?
public class CheckWeatherTask extends AsyncTask<String, Void, String>
{

    public String proxyURL;
    public String dummyString;
    public boolean useProxy;

    public String DEBUG_TAG = "JLamPractice";

    // move to shared class to allow all to access
    private static final String APPID="cbd7cd2ad1367e68fdfd3fe2009d3d6d";
    private static final String baseUrl="http://api.openweathermap.org/data/2.5/weather";
    private static final String queryStringTemplate="?q=\"%s\"&APPID=%s";

    // should be specified activity class so it
    // can call the appropriate function
    public CheckWeatherPro activity;

    public void setActivity(CheckWeatherPro activity)
    {
        this.activity = activity;
    }

    public String ConstructUrl(String location)
    {
        location = location.replace(" ", "%20");
        String queryString = String.format(queryStringTemplate, location, APPID);
        return baseUrl + queryString;
    }

    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException
    {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return  new String(buffer);
    }

    public String downloadUrl(String myurl) throws IOException
    {
        Log.d("downloadUrl", "In downloadUrl");
        InputStream is=null;

        // todo: how to allow dynamic length?
        int len = 1024;
        try
        {
            URL url = new URL(myurl);
            Log.d("downloadUrl", "target URL:" + url.toString());
            HttpURLConnection conn = null;
                /*if(useProxy)
                {*/
            //Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.houston.hp.com", 8080));
            //conn = (HttpURLConnection)url.openConnection(proxy);
                /*
                }
                else
                {
                    conn = (HttpURLConnection)url.openConnection();
                }*/
            // something about activity failing removing?
            conn = (HttpURLConnection)url.openConnection();

            conn.setReadTimeout(30000 /* milliseconds */);
            // which one needs to go up?!
            conn.setConnectTimeout(30000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            //int response = conn.getResponseCode();
            //Log.d(DEBUG_TAG, "The response is: " + response);
            is = conn.getInputStream();

            // convert to string
            String contentAsString = readIt(is, len);
            return contentAsString;

        }
        finally
        {
            if(is != null)
            {
                is.close();
            }
        }

    }



    @Override
    protected String doInBackground(String... location)
    {
        Log.d("doInBackground", "Task started.");
        try
        {
            // believe it's failing here...
            if(this.activity == null)
            {
                throw new NullPointerException("CheckWeatherPro type activity not specified.");
            }


            this.activity.UpdateProgress("Getting weather info");
            String targetUrl = this.ConstructUrl(location[0]);
            Log.d("doInBackground", "targetUrl:" + targetUrl);

            return downloadUrl(targetUrl);
        }
        catch(IOException e)
        {
            return "Error - Unable to get data." + e.getMessage();
        }
        catch (Exception e)
        {
            return "Error - " + e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result)
    {
        // update UI with result
        String updateText = "";
        WeatherLocation WeatherLocation;
        if(result.length() == 0)
        {
            WeatherLocation = new WeatherLocation("Error - No data.");
        }
        else
        {
            if(result.startsWith("Error -"))
            {
                Log.d("onPostExecute", "Parsing error result...");
                WeatherLocation = new WeatherLocation(result);
            }
            else
            {
            // need to diff between exception and json data...
                Log.d("onPostExecute", "Parsing successful result...");
                WeatherLocation = this.ParseWeatherInfo(result);

            // todo: parse only if success
            // for now just fill up all entries with info from result
            // will do parse later
            }
            //updateText = result;

            //
        }

        // callback
        // main activity will pass along to fragment
        // anyway to do this layout-agnostic?
        // does this block?
        this.activity.UpdateResult(WeatherLocation);

        /*TextView textView = (TextView)findViewById(R.id.output_area);
        textView.setText(result);*/
    }

    protected WeatherLocation ParseWeatherInfo(String textData)
    {
        WeatherLocation weatherLocation = new WeatherLocation();

        // json parsing goes here
        // load into json object

        // is an array per spec?

        // todo:for entries like Hong Kong (no location name) need to display country in location spot
        // todo: multiplexing WeatherLocation type to hold bad result is bad. need to redesign to avoid this
        try
        {
            Log.d("ParseWeatherInfo","Attempting to parse JSON response");
            JSONObject jsonObject = new JSONObject(textData);
            // get first node
            //JSONObject firstNode = jsonArray.getJSONObject(0);

            // pull out json objects that has weather info
            Log.d("ParseWeatherInfo", "Getting weather array and first object");
            JSONArray weather = jsonObject.getJSONArray("weather");

            weatherLocation.setWeather(weather.getJSONObject(0).getString("main"));

            // null string?
            Log.d("ParseWeatherInfo", "Getting location name (if present?)");
            if(jsonObject.has("name"))
            {
                weatherLocation.setLocation(jsonObject.getString("name"));
            }

            // this should always be present
            Log.d("ParseWeatherInfo", "Getting country.");
            weatherLocation.setCountry(jsonObject.getJSONObject("sys").getString("country"));

            // for now display in kelvin
            Log.d("ParseWeatherInfo", "Getting temperature.");
            weatherLocation.setTemperature(Double.toString(jsonObject.getJSONObject("main").getDouble("temp")));
            // todo: add debug field?

            // get the id of the location. this is different from sql row id
            Log.d("ParseWeatherInfo", "Getting location id.");
            weatherLocation.setLocation_id((jsonObject.getInt("id")));
            Log.d("ParseWeatherInfo", "id value:" + Integer.toString(jsonObject.getInt("id")));
        }
        catch(JSONException e)
        {
            Log.d("ParseWeatherInfo", "Exception thrown - " + e.getMessage());
            weatherLocation = new WeatherLocation(e.getMessage());
            return weatherLocation;
        }

        return weatherLocation;
    }

}



