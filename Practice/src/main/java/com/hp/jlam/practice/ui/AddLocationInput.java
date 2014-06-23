package com.hp.jlam.practice.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.hp.jlam.practice.R;

// first get fragments working
// then get rotational layout working? (horizontal should look nice?)


/**
 * Created by lamjon on 1/30/14.
 */
public class AddLocationInput extends Fragment
    implements View.OnClickListener
{
    OnBeginGetWeatherListener mGetWeatherCallback;

    public interface OnBeginGetWeatherListener
    {
        public void onBeginGetWeather(String location);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weatherpro, container, false);
        Button button = (Button)view.findViewById(R.id.buttonGetWeather);
        button.setOnClickListener(this);
        return view;
    }

    // do i move logic ui here?
    @Override
    public void onClick(View view)
    {
        // only 1 button so call handler directly
        onClickGetWeatherButtonPro(view);
    }

    public void onClickGetWeatherButtonPro(View view)
    {
        //TextView textView = (TextView)getView().findViewById(R.id.output_area);
        //textView.setText("Getting weather info...");
        //this.getActivity()

        // get text from text field

        EditText editText = (EditText)getView().findViewById(R.id.input_location_text);
        String location = editText.getText().toString();

        //when fragment is created
        // where to put?!
        // create an

        // call the implementation in the main activity
        mGetWeatherCallback.onBeginGetWeather(location);

        /**
        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new CheckWeatherTask().execute(location);
        } else
        {
            textView.setText("Unable to connect.");
        }*/
    }

    public void EnableButton(final boolean enableButton)
    {
        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Button button = (Button)getView().findViewById(R.id.buttonGetWeather);
                button.setEnabled(enableButton);

            }
        });
    }
}
