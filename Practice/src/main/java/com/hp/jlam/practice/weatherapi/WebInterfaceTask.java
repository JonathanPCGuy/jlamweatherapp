package com.hp.jlam.practice.weatherapi;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

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
public class WebInterfaceTask extends AsyncTask<Integer, Void, String> {

    private static final String APPID="cbd7cd2ad1367e68fdfd3fe2009d3d6d";
    private static final String baseUrl="http://api.openweathermap.org/data/2.5/";
    private static final String forecastUrlQueryStringTemplate="forecast/daily?id=%d&cnt=%d&APPID=%s";
    private static final String queryStringTemplate="?q=\"%s\"&APPID=%s";

    // to determine, input and output types.
    // input: location)id

    // to really make it generic i would define an interface that
    // other activities would have to implement
    // for now hardcode for 1 type (DetailedWeatherInfo)
    // todo: create interface

    // interface instance to allow update of results
    private APICallResults detailedWeatherInfo;

    public void SetParentActivity(APICallResults detailedWeatherInfo)
    {
        this.detailedWeatherInfo = detailedWeatherInfo;
    }

    private boolean isSuccess = true;
    private Exception taskException = null;

    // move this to separate class?
    private String ConstructFutureForecastURL(Integer location, Integer days)
    {
        //location = location.replace(" ", "%20");
        String formattedUrlQueryString = String.format(forecastUrlQueryStringTemplate
            ,location, days, APPID);
        return baseUrl + formattedUrlQueryString;

    }

    @Override
    protected String doInBackground(Integer... location)
    {
        try
        {
            // need to ensure at least 1 location is specified
            Integer theLocation = location[0];
            String targetUrl = this.ConstructFutureForecastURL(theLocation, 5);
            return downloadUrl(targetUrl);
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
            // process raw json
            // still need to do validation
            // do we process raw json her? how to let other things process it?
            // any better way to to this to pass result back? interface!
            this.detailedWeatherInfo.UpdateResults(result);
        }
        else
        {
            Log.d("onPostExecute", "Error while attempting to get forecast.");
            if(this.taskException != null)
            {
                Log.d("onPostExecute", "Exception thrown:" + taskException.getClass().getName());
                if(taskException.getMessage() == null) {
                    this.detailedWeatherInfo.UpdateWithError(taskException.getClass().getName());
                }
                else {
                    this.detailedWeatherInfo.UpdateWithError(taskException.getMessage());
                }
            }
            else
            {
                this.detailedWeatherInfo.UpdateWithError("Some error occurred.");
            }

        }
    }

    // need to eliminate duplicate functions and consolidate
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

            //HttpClient httpClient = new DefaultHttpClient();
            //HttpResponse = httpClient.execute(new HttpGet(myurl));
            HttpURLConnection conn = null;
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
            // issue is that reading to end up stream not working
            // how to make it really work?
            String contentAsString = readIt(is);
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

    public String readIt(InputStream stream) throws IOException, UnsupportedEncodingException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));

        String line = "";
        StringBuilder sb = new StringBuilder();

        // is this ok if we're not expecting anything else? don't need to do while?
        if((line = br.readLine()) != null)
        {
            sb.append(line);
        }

        return sb.toString();
    }
}
