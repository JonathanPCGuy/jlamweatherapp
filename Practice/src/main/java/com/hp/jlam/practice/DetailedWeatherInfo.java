package com.hp.jlam.practice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.hp.jlam.practice.weatherapi.APICallResults;
import com.hp.jlam.practice.weatherapi.FutureDailyForecast;
import com.hp.jlam.practice.weatherapi.ResultsSerializer;
import com.hp.jlam.practice.weatherapi.WebInterfaceTask;

import org.json.JSONException;

public class DetailedWeatherInfo extends ActionBarActivity
        implements APICallResults, AdapterView.OnItemSelectedListener {

    private WeatherInfoLocation weatherInfoLocationFragment;
    private WeatherInfoLocationDetail weatherInfoLocationDetailFragment;

    private WebInterfaceTask interfaceTask;

    private double location_lat;
    private double location_lon;

    private ProgressDialog pd;
    private int[] arrayForecastSelectionValues;

    // what if you had multiple selector? how do you hook up a specific one?
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
    {
        // get selected item

        // start task
        // need to get value of item selected and call the function
        // below is just to determine if roundtrip works, will need to remove this later
        StartUpdateTask(arrayForecastSelectionValues[pos]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_weather_info);

        // finViewById not equal null?

        if(savedInstanceState != null)
        {
            return;
        }
        arrayForecastSelectionValues = getResources().getIntArray(R.array.forecast_display_values);

        /**
         * 1. load and fill in top fragment with info on selected city
         * 2. load and fill in bottom fragment with forecast type
         *    right now only have (5, 7 day) forecast
         *
         */
        // todo: get all the app compat stuff sorted out

        // for now do this here
        // will we always get the value in this function?

        Intent intent = getIntent();

        this.location_lat = intent.getDoubleExtra(ExtraConstants.EXTRA_LOCATION_LAT, -9999);
        this.location_lon = intent.getDoubleExtra(ExtraConstants.EXTRA_LOCATION_LON, -9999);
        // TODO: add extra to pass in location name, or maybe pull it from location id api call?
        // if id changes then we're in trouble...

        // add to main layout
        Log.d("onCreate DetailedWeatherInfo", "About to inflate top and bottom fragments.");

        // can i just do new or should i store in variables?
        this.weatherInfoLocationFragment = new WeatherInfoLocation();

        this.weatherInfoLocationFragment.SetLocationInfo(intent.getStringExtra(ExtraConstants.EXTRA_LOCATION_LOCATION),
                intent.getStringExtra(ExtraConstants.EXTRA_LOCATION_COUNTRY));

        this.weatherInfoLocationDetailFragment = new WeatherInfoLocationDetail();
        // ok, it works, but one fragment is covering the other

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.add(R.id.detailedWeatherContainer, this.weatherInfoLocationFragment, "weatherInfoLocation");

        // overlapping (or replace?) issue...

        fragmentTransaction.add(R.id.detailedWeatherContainer, this.weatherInfoLocationDetailFragment, "weatherInfoLocationDetail");

        fragmentTransaction.commit();

        // ok we now have id


        // for dev purposes, first jam in a location to the task to run
        // later on we'll use a real id
        // toasts?
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        /*todo:
            1. if we already have data


         */

        // todo: determine if this is necessary
        //StartUpdateTask(5);

    }

    protected void StartUpdateTask(int days)
    {
        // kick off backgroundtask to call android api
        pd = ProgressDialog.show(this, "Please wait...", "Getting weather forecast.");


        interfaceTask = WebInterfaceTask.CreateFutureForecastTask(this.location_lat, this.location_lon, days);
        interfaceTask.SetParentActivity(this);
        interfaceTask.SetProgressDialog(pd);
        interfaceTask.execute();
        //temporarily disable show spinner
        //this.ShowSpinner(true);
    }

    public void ShowSpinner(boolean showSpinner)
    {
        //todo: fix this later
        //this.weatherInfoLocationDetailFragment.ShowSpinner(showSpinner);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detailed_weather_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void UpdateResults(String jsonData)
    {
        try
        {
            FutureDailyForecast futureDailyForecast = ResultsSerializer.ProcessFutureDailyForecastJSON(jsonData);
            this.weatherInfoLocationDetailFragment.LoadWeatherResults(futureDailyForecast);
            this.ShowSpinner(false);
        }
        catch(JSONException e)
        {
            UpdateWithError(e.getMessage());
        }
    }

    @Override
    public void UpdateWithError(String errorString)
    {
        // anyway to show popup?
        // for now just log
        Log.e("UpdateWithError", errorString);
        this.ShowSpinner(false);
    }

    public void onNewWeatherForecastLengthSelected()
    {

    }
}
