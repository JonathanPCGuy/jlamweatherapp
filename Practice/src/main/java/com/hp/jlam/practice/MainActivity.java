package com.hp.jlam.practice;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    public final static String EXTRA_MESSAGE = "com.hp.jlam.practice.MESSAGE";
    public final static String EXTRA_LOCATION = "com.hp.jlam.practice.LOCATION";
    public final static String EXTRA_ADD_LOCATION ="IsAddRequest";
    public final int REQUEST_CODE_ADD = 1;

    private final static String baseURI = "http://api.openweathermap.org/data/2.5/weather";
    private final static String queryString ="q=%s";

    protected ArrayList<WeatherLocation> weatherLocations;
    protected WeatherLocationAdapter weatherLocationAdapter;
    private ListView locationListView;

    /*public void sendMessage(View view)
    {
        Intent intent = new Intent(this, DisplayMessageActivity.class);

        EditText editText = (EditText)findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);

        // add notification

        // later we'll do ongoing notification
    }*/

    public void onClickGetWeather(View view)
    {
        /*Intent intent = new Intent(this, CheckWeatherActivity.class);

        EditText editText = (EditText)findViewById(R.id.edit_message);
        String location = editText.getText().toString();
        intent.putExtra(EXTRA_LOCATION, location);
        startActivity(intent);*/

        // String formattedQueryString = String.format(queryString, location);

        // pass string to CheckWeather activity

        // build URI

        // start task to get info

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }*/

        // look for save data
        Log.v("onCreate MainActivity", "In MainActivity onCreate");

        WeatherAppStorage weatherAppStorage = new WeatherAppStorage(this);

        Log.d("onCreate MainActivity", "Getting write access to db...");
        // need to do this to ensure tables are present if not created
        weatherAppStorage.getWritableDatabase();

        // query db for any existing saved entries
        Log.d("MainActivity", "Reading locations from the table (if any)...");

        // how is the onUpdate triggered? want to clear this out later...
        this.weatherLocations = weatherAppStorage.getAllWeatherLocations();


        // need to do this special for loop style more often
        for(WeatherLocation wl: weatherLocations)
        {
            Log.v("MainActivity",
                    "Location:" + wl.getLocation() + " Country:" + wl.getCountry() + "location id:" + wl.getLocation_id());
        }

        Log.d("MainActivity - onCreate", "Setting up listview");
        this.weatherLocationAdapter = new WeatherLocationAdapter(
                this,
                R.layout.list_view_weather_result_item,
                this.weatherLocations);

        locationListView = (ListView)this.findViewById(R.id.listViewLocations);

        locationListView.setAdapter(weatherLocationAdapter);
        locationListView.setOnItemClickListener(new OnClickWeatherLocationItem());

        Log.d("MainActivity - onCreate", "Signal list view that contents may have been updated.");
        this.UpdateWeatherLocationListView();




        // first let's insert some rows into db to ensure correct operation
        /*
        Log.d("MainActivity", "Inserting entry into table...");
        weatherAppStorage.addWeatherLocation(
                new WeatherLocation("Hong Kong", "China"));
        weatherAppStorage.addWeatherLocation(
                new WeatherLocation("Houston", "United States of America"));
        weatherAppStorage.addWeatherLocation(
                new WeatherLocation("London", "GB")
        );*/




        // output results to log first

        // later on we'll link it to UI (does it have to be manual?)


        // if save data found load and display

        // todo: in background get weather data and display

    }

    private void InitializeStorage()
    {
        // should be done if first time
        // or if db needs to be recreated

        // should this be separate class?
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity_actions, menu);
        return true;
    }


    // want to link list view click to load dummy activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            /*case R.id.action_email:
                EditText editText = (EditText)findViewById(R.id.edit_message);
                String message = editText.getText().toString();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_TEXT, message);

                startActivity(intent);
                return true;*/
            case R.id.action_weatherpro:
                Intent weatherIntent = new Intent(this, AddLocationActivity.class);
                weatherIntent.putExtra(EXTRA_ADD_LOCATION, true);
                startActivityForResult(weatherIntent, REQUEST_CODE_ADD);
                //startActivity(weatherIntent);
                return true;

            case R.id.action_debug:
                Intent debugIntent = new Intent(this, DebugActivity.class);
                startActivity(debugIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.v("MainActivity - onActivityResult", "In onActivityResult");

        if(data == null)
        {
            Log.d("MainActivity - onActivityResult", "Warning - Intent data is null!");
            return;
        }

        if(requestCode == REQUEST_CODE_ADD)
        {
            if(resultCode == RESULT_OK)
            {
                // success

                // first confirm in log that the data gets back from intent
                // todo: duplicate check?
                // we can write it here?

                //get id
                long newItemId = data.getLongExtra("newItemId", -1L);
                if(newItemId == -1)
                {
                    // nothing added
                }
                else
                {
                     // item added, get id from db
                    WeatherAppStorage weatherAppStorage = new WeatherAppStorage(this);
                    WeatherLocation location = weatherAppStorage.getWeatherLocation(newItemId);

                    // add it to the list


                    ListView listView = (ListView)findViewById(R.id.listViewLocations);
                    this.weatherLocations.add(location);
                    this.UpdateWeatherLocationListView();

                    Log.d("onActivityResult", "Added location:" + location.getLocation());
                    Log.d("onActivityResult", "Added country:" + location.getCountry());
                }
            }
            else
            {
                // error or abort or no data
                Log.d("onActivityResult", "resultCode is not 0; is " + resultCode);
            }
        }
    }

    private void UpdateWeatherLocationListView()
    {
        TextView textView = (TextView)this.findViewById(R.id.textViewErrorMessage);
        ListView listView = (ListView)findViewById(R.id.listViewLocations);

        this.weatherLocationAdapter.notifyDataSetChanged();

        if(this.weatherLocations.isEmpty())
        {
            textView.setVisibility(View.VISIBLE);
            this.locationListView.setVisibility(View.GONE);
        }
        else
        {
            textView.setVisibility(View.GONE);
            this.locationListView.setVisibility(View.VISIBLE);
        }
    }

    public void AddWeatherLocation(WeatherLocation location)
    {
        // update array
        this.weatherLocations.add(location);

        this.UpdateWeatherLocationListView();
        // don't need to update db since we already did that in child activity

        // refresh list
        //this.locationListView.getAdapter().notify();

    }

    public void RemoveWeatherLocation(WeatherLocation location)
    {
        // todo: implement delete stuff later
    }

    /**
     * A placeholder fragment containing a simple view.
     */

    /**
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }*/

}
