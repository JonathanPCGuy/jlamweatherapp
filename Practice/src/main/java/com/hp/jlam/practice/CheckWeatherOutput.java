package com.hp.jlam.practice;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;

/**
 * Created by lamjon on 1/30/14.
 */
public class CheckWeatherOutput extends Fragment

{
    // since the API only returns 1 result (does it really?) we can
    // safely store 1 location as the location to "add" (if in that mode)



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weatherresult, container, false);
        //TextView textView = (TextView)view.findViewById(R.id.textViewMessage);
        //textView.setText("Ready");
        return view;
    }

    // why works sometimes but not other times? need to fix completely...
    public void UpdateProgress(final String message)
    {

        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView textView = (TextView)getView().findViewById(R.id.textViewMessage);
                // shouldn't have to set this on every call?
                textView.setVisibility(View.VISIBLE);
                ((TableLayout)getView().findViewById(R.id.tableResults)).setVisibility(View.GONE);
                textView.setText(message);
            }
        });
    }

    public void ShowSpinner()
    {
        this.ToggleSpinner(true);
    }

    public void HideSpinner()
    {
        this.ToggleSpinner(false);
    }

    private void ToggleSpinner(final boolean showSpinner)
    {
        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ProgressBar progressBar = (ProgressBar)getView().findViewById(R.id.progressBar);
                TextView textView = (TextView)getView().findViewById(R.id.textViewMessage);
                // shouldn't have to set this on every call?

                if(showSpinner)
                {
                    progressBar.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                    ((TableLayout)getView().findViewById(R.id.tableResults)).setVisibility(View.GONE);
                }
                else
                {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    public void UpdateClickableResults(final WeatherLocation result)
    {
        // make the result more pretty and clickable
        // when clicked it should add the result back to the main view to show
        // a quick forecast
        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {


            }
        });
    }

    private void CreateClickableResult(WeatherLocation result)
    {
        /*LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1);



        LinearLayout linearLayout = new LinearLayout(this.getActivity());*/

        // shove in the weather info

        // make it clickable

        // attach it to the empty container reserved for results

    }

    public void onClickWeatherResult(View view)
    {
        // only 1 button so call handler directly
        //onClickGetWeatherButtonPro(view);
    }

    // can't directly link to button?

    public void OnClickAddResult(View view)
    {

    }


    // any other way to not use final?
    public void UpdateResults(final WeatherLocation result)
    {
        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView textView = (TextView)getView().findViewById(R.id.textViewMessage);
                // shouldn't have to set this on every call?
                // is the result even going through?
                if(!result.getIsSuccess())
                {
                    Log.d("UpdateResults", "Not success, displaying error.");
                    textView.setVisibility(View.VISIBLE);
                    ((TableLayout)getView().findViewById(R.id.tableResults)).setVisibility(View.GONE);
                    textView.setText(result.getErrorMessage());
                    Log.d("UpdateResults", result.getErrorMessage());

                    ((Button)getView().findViewById(R.id.buttonAddLocation)).setEnabled(false);
                }
                else
                {
                    Log.d("UpdateResults", "Much success, very weather.");
                    textView.setVisibility(View.GONE);
                    ((TableLayout)getView().findViewById(R.id.tableResults)).setVisibility(View.VISIBLE);

                    // how to easily set values in view? have to getView every single one?
                    ((TextView)getView().findViewById(R.id.textFutureForecastLocation)).setText(result.getFullLocation());
                    ((TextView)getView().findViewById(R.id.textTemp)).setText(result.getTemperature());
                    ((TextView)getView().findViewById(R.id.textDescription)).setText(result.getWeather());
                    Log.d("UpdateResults", result.getFullLocation());
                    Log.d("UpdateResults", result.getTemperature());
                    Log.d("UpdateResults", result.getWeather());
                    ((Button)getView().findViewById(R.id.buttonAddLocation)).setEnabled(true);


                }
            }
        });

        //this.weatherResult = result;

    }
}
