package com.hp.jlam.practice.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.hp.jlam.practice.R;
import com.hp.jlam.practice.TempDisplayUnit;
import com.hp.jlam.practice.WeatherPrefs;
import com.hp.jlam.practice.ui.activities.DetailedWeatherActivity;
import com.hp.jlam.practice.weatherapi.DailyForecast;
import com.hp.jlam.practice.weatherapi.FutureDailyForecast;

import java.text.SimpleDateFormat;

/**
 *
 * can be 5-7 day forecast, weather radar, etc.
 * for now forecast only
 *
 * this is the bottom part of the view.
 * displays the 5, 7, etc. day forecast
 * get data from the parent container class
 *
 */
public class WeatherInfoLocationDetail extends Fragment
{

    /*

        1. on load "hide" forecast and load
        2. show the spinning icon
        3. after load load data and reveal



     */

    private LinearLayout futureForecastDayContainer;

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.fragment_future_forecast, container, false);

        // how to add child?
        futureForecastDayContainer = (LinearLayout)view.findViewById(R.id.forecastData);
        //LinearLayout layout = (LinearLayout)view.findViewById(R.id.forecastData);
        //LoadDummyData(futureForecastDayContainer);
        // refresh layout
        Spinner spinner = (Spinner)view.findViewById(R.id.spinnerForecastOptions);
        // does this trigger an event?
        spinner.setOnItemSelectedListener((DetailedWeatherActivity)this.getActivity());
        spinner.setSelection(1);

        return view;

    }

    public void LoadWeatherResults(FutureDailyForecast futureDailyForecast)
    {
        // need to "invoke"?

        // 1. need to wipe existing view, if any
        futureForecastDayContainer.removeAllViewsInLayout();



        // add stuff to the layout
        for(int i = 0; i < futureDailyForecast.futureForecast.size(); i++)
        {
            View view = CreateDayForecastItem(futureDailyForecast.futureForecast.get(i),
                    futureForecastDayContainer);
            // this works!
            // params from xml are ignored if added this way
            // need to respecify stuff
            futureForecastDayContainer.addView(view,
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        }

    }

    // creates the ui object for a single day to be
    // appended to the container
    // need to put in real return type and input type
    // for now just high and low temps
    // get weather state in later
    public View CreateDayForecastItem(DailyForecast dailyForecast, ViewGroup parent)
    {
        // do i need to a layout file? can i just create one?
        // how to get context?
        // to do: fix
        //View forecastItemView = getLayoutInflater().inflate(R.layout.weather_day_forecast_item, parent, false);
        View forecastItemView = View.inflate(getActivity().getApplicationContext(),R.layout.weather_day_forecast_item, null);

        //get the desired units of measurement
        // could be optimized further by getting the value elsewhere?
        TempDisplayUnit tempDisplayUnit = WeatherPrefs.GetTempDisplayUnit(this.getActivity());
        //GridLayout.inflate(getActivity().getApplicationContext(),R.layout.weather_day_forecast_item, null);
        // this doesn't work because previously added layout have the same id so it's finding the 1st one
        // put in forecast data

        TextView textViewTempHigh = (TextView)forecastItemView.findViewById(R.id.textViewTempHigh);
        textViewTempHigh.setText(dailyForecast.GetHighDisplayString(tempDisplayUnit));

        TextView textViewTempLow = (TextView)forecastItemView.findViewById(R.id.textViewTempLow);

        textViewTempLow.setText(dailyForecast.GetLowDisplayString(tempDisplayUnit));
        // return view; upon return this should be added to the 5-7 day forecast area

        TextView textViewDate = (TextView)forecastItemView.findViewById(R.id.textViewDate);
        TextView textViewDay = (TextView)forecastItemView.findViewById(R.id.textViewDay);

        // todo: this string should be localized per region. for now do MM-DD

        SimpleDateFormat dateFormat = new SimpleDateFormat("M-dd");
        SimpleDateFormat dateFormatDay = new SimpleDateFormat("EEE");

        String dateString = dateFormat.format(dailyForecast.date.getTime());
        String dateDayString = dateFormatDay.format(dailyForecast.date.getTime());

        textViewDate.setText(dateString);
        textViewDay.setText(dateDayString);

        //new LinearLayout(this.getParentFragment().getView());
        return forecastItemView;
    }

}
