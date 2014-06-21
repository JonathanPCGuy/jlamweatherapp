package com.hp.jlam.practice.weatherapi;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by lamjon on 4/10/2014.
 */

// do i need progress? could use to send status messages?
public class WebInterfaceTask extends AsyncTask<Void, Void, String> {

    private static final String APPID="cbd7cd2ad1367e68fdfd3fe2009d3d6d";
    private static final String baseUrl="http://api.openweathermap.org/data/2.5/";
    private static final String forecastUrlQueryStringTemplate="forecast/daily?id=%d&cnt=%d&APPID=%s";
    private static final String forecastWithLatLonQueryStringTemplate="forecast/daily?lat=%g&lon=%g&cnt=%d&APPID=%s";
    private static final String queryStringTemplate="weather?q=\"%s\"&APPID=%s";

    private static final String latLonTemplate = "lat=%g&lon=%g";
    private static final String locationQueryTemplate = "q=\"%s\"";

    private static final String weatherLocationTemplate = "weather?%s&APPID=%s";

    // to determine, input and output types.
    // input: location)id

    // to really make it generic i would define an interface that
    // other activities would have to implement
    // for now hardcode for 1 type (DetailedWeatherActivity)

    // interface instance to allow update of results
    private APICallResults activityReceivingResults;

    public enum WebTaskType
    {
        UNKNOWN,
        LOCATION_INFO,
        FUTURE_FORECAST
    }

    private boolean isSuccess = true;

    public boolean get_isSuccess() {return this.isSuccess;}

    private Exception taskException = null;
    private WebTaskType webTaskType = WebTaskType.UNKNOWN;
    private String targetURL;
    private ProgressDialog pd;

    // constructor logic below

    public void SetParentActivity(APICallResults activityReceivingResults)
    {
        this.activityReceivingResults = activityReceivingResults;
    }

    public void SetProgressDialog(ProgressDialog pd)
    {
        this.pd = pd;
    }

    public static String ConstructFutureForecastURL(Double lat, Double lon, Integer days)
    {
        //location = location.replace(" ", "%20");
        String formattedFullQueryString = String.format(
                forecastWithLatLonQueryStringTemplate,
                lat,
                lon,
                days,
                APPID);
        return baseUrl + formattedFullQueryString;

    }

    public static String ConstructLocationWeatherURL(String locationQueryString)
    {
        String formattedQuery = String.format(locationQueryTemplate
                ,locationQueryString.replace(" ", "%20"));

        String formattedFullQueryString = String.format(
                weatherLocationTemplate,
                formattedQuery,
                APPID);

        return baseUrl + formattedFullQueryString;
    }

    public static String ConstructLocationWeatherURL(Double lat, Double lon)
    {
        String formattedQuery = String.format(latLonTemplate, lat, lon);

        String formattedLocationWeatherQueryString = String.format(
                weatherLocationTemplate,
                formattedQuery,
                APPID);

        return baseUrl + formattedLocationWeatherQueryString;
    }

    private WebInterfaceTask(WebTaskType type)
    {
        this.webTaskType = type;
        this.pd = null;
    }

    public static WebInterfaceTask CreateFutureForecastTask(Double lat, Double lon, Integer days)
    {
        Log.d("CreateFutureForecastTask","");
        WebInterfaceTask webInterfaceTask = new WebInterfaceTask(WebTaskType.FUTURE_FORECAST);
        webInterfaceTask.targetURL = ConstructFutureForecastURL(lat, lon, days);
        return webInterfaceTask;
    }

    public static WebInterfaceTask CreateWeatherLocationTask(String locationQueryString)
    {
        Log.d("CreateWeatherLocationTask","Creating a current weather task, query based.");
        WebInterfaceTask webInterfaceTask = new WebInterfaceTask(WebTaskType.LOCATION_INFO);
        webInterfaceTask.targetURL = ConstructLocationWeatherURL(locationQueryString);
        return webInterfaceTask;
    }

    public static WebInterfaceTask CreateWeatherLocationTask(Double lat, Double lon)
    {
        Log.d("CreateWeatherLocationTask", "Creating a current weather task, lat/lon based.");
        WebInterfaceTask webInterfaceTask = new WebInterfaceTask((WebTaskType.LOCATION_INFO));
        webInterfaceTask.targetURL = ConstructLocationWeatherURL(lat, lon);
        return webInterfaceTask;
    }

    // worker logic below


    // anyway to not have to specify input? or I just ignore it?
    @Override
    protected String doInBackground(Void... input)
    {
        try
        {
            return downloadUrl(this.targetURL);
        }
        catch(Exception e)
        {
            this.taskException = e;
            isSuccess = false;
            return "Error";
        }
    }

    @Override
    protected void onPostExecute(String result)
    {
        if(isSuccess == true)
        {
            Log.d("onPostExecute", "Successfully got forecast");
            if(this.activityReceivingResults != null)
            {
                this.activityReceivingResults.UpdateResults(result);
            }
        }
        else
        {
            Log.d("onPostExecute", "Error while attempting to get forecast.");
            String errorMessage;// = new String();
            if(this.taskException != null)
            {
                Log.d("onPostExecute", "Exception thrown:" + taskException.getClass().getName());
                if(taskException.getMessage() == null)
                {
                    errorMessage = taskException.getClass().getName();
                }
                else
                {
                    errorMessage = taskException.getMessage();
                }
            }
            else
            {
                errorMessage = "Some error occurred.";
            }

            if(this.activityReceivingResults != null)
            {
                this.activityReceivingResults.UpdateWithError(errorMessage);
            }
        }

        if(pd != null)
        {
            pd.dismiss();
        }
    }

    public String downloadUrl(String myurl) throws IOException
    {
        Log.d("downloadUrl", "In downloadUrl");
        InputStream is=null;

        // todo: how to allow dynamic length?
        try
        {
            URL url = new URL(myurl);
            Log.d("downloadUrl", "target URL:" + url.toString());

            // todo: better way to do url vs parameters? is there a separate option?
            HttpURLConnection conn = null;
            conn = (HttpURLConnection)url.openConnection();

            conn.setReadTimeout(30000 /* milliseconds */);
            conn.setConnectTimeout(30000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setRequestProperty("Accept-Encoding", "");
            conn.setRequestProperty("Accept", "application/json");
            // Starts the query
            conn.connect();
            int responseCode = conn.getResponseCode();
            is = conn.getInputStream();
            // convert to string
            // issue is that reading to end up stream not working
            // how to make it really work?
            String contentAsString = readIt(is);
            Log.d("downloadUrl", "Able to successfully get download.");
            return contentAsString;

        }
        finally
        {
            if(is != null)
            {
                is.close();
            }
            Log.d("downloadUrl", "Done with downloading.");
        }

    }

    public String readIt(InputStream stream) throws IOException, UnsupportedEncodingException
    {
        Log.d("readIt", "Reading stream.");
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));

        String line = "";
        StringBuilder sb = new StringBuilder();

        // TODO: code below will only read 1 line. if web api uses multiple lines it will
        // break. need to find a better way of doing this
        if((line = br.readLine()) != null)
        {
            sb.append(line);
        }
        Log.d("readIt", "Done reading stream.");
        return sb.toString();
    }
}
