package com.hp.jlam.practice;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class DebugActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.debug, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_debug, container, false);

            // load in test data
            ArrayList<WeatherLocation> weatherLocations = new ArrayList<WeatherLocation>();
            WeatherLocation singleResult = new WeatherLocation();
            singleResult.setLocation("Houston");
            singleResult.setCountry("USA");
            weatherLocations.add(singleResult);
            singleResult = new WeatherLocation();
            singleResult.setLocation("Hong Kong");
            singleResult.setCountry("China");
            weatherLocations.add(singleResult);

            WeatherLocationAdapter weatherLocationAdapter = new WeatherLocationAdapter(
                    this.getActivity(),
                    R.layout.list_view_weather_result_item,
                    weatherLocations);

            ListView listViewInFragment = (ListView)rootView.findViewById(R.id.listViewDebug);

            listViewInFragment.setAdapter(weatherLocationAdapter);

            return rootView;
        }
    }

}
