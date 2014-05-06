package com.hp.jlam.practice;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class WeatherInfoLocation extends Fragment {

    /**
     *
     *     public View onCreateView(LayoutInflater inflater, ViewGroup container,
     Bundle savedInstanceState)

     this is the top part of the detailed view. holds the location info

     */

    private String location;
    private String country;


    public void SetLocationInfo(String location, String country)
    {
        this.location = location;
        this.country = country;
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_future_forecast_location, container, false);

        // am i allow to do this here?
        TextView textView = (TextView)view.findViewById(R.id.textFutureForecastLocation);
        textView.setText(this.location);

        textView = (TextView)view.findViewById(R.id.textFutureForecastCountry);
        textView.setText(this.country);


        return view;

    }


}
