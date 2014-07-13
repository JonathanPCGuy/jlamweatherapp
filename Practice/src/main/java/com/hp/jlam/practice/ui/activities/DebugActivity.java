package com.hp.jlam.practice.ui.activities;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.hp.jlam.practice.ExtraConstants;
import com.hp.jlam.practice.IntentConstants;
import com.hp.jlam.practice.R;
import com.hp.jlam.practice.WeatherPrefs;
import com.hp.jlam.practice.WeatherUpdateIntentService;
import com.hp.jlam.practice.WeatherUpdateLocation;
import com.hp.jlam.practice.receivers.StartWeatherUpdateReceiver;

import java.util.Calendar;

public class DebugActivity extends ActionBarActivity {

    private NotificationManager mNM;
    private NotificationCompat.Builder notification;
    private static final int NOTIFICATION_ID = R.string.local_service_started;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        /*
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }*/

        // in the future, if this is not running, how will broadcastreceive work?
        // for now, just get the broadcast roundtrip working
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        this.registerReceiver(testReceiver, new IntentFilter(IntentConstants.WEATHER_UPDATE));
    }

    @Override
    public void onDestroy()
    {
        this.unregisterReceiver(testReceiver);
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

    public void onClickButtonOutput(View view)
    {
        Log.d("DebugActivity", "onClickButtonOutput clicked.");
        // todo: call the intent service
        // block the ui thread? don't care this is just for debugging

        // we want to do the following:
        /*
            1. button fires off broadcast
            2. broadcast calls intent service
            3. intent service gets the data
            4. the broadcastreceived event gets called
            5. the notification gets updated
         */

        // do i need to do this in my own thread?
        // instead of calling the service directly we'll call the alarm task
        // set it to call now


        //SetUpdateAlarm();
        /*
        Intent serviceIntent = new Intent(this, WeatherUpdateIntentService.class);
        serviceIntent.putExtra(ExtraConstants.LOCATION_LAT, 51.50853);
        serviceIntent.putExtra(ExtraConstants.LOCATION_LON, -0.12574);
        startService(serviceIntent);
        */
        Intent serviceIntent = new Intent(this, WeatherUpdateIntentService.class);
        WeatherUpdateLocation weatherUpdateLocation = WeatherPrefs.GetWeatherUpdateLocation(this);
        serviceIntent.putExtra(ExtraConstants.LOCATION_LAT, weatherUpdateLocation.lat);
        serviceIntent.putExtra(ExtraConstants.LOCATION_LON, weatherUpdateLocation.lon);
        startService(serviceIntent);

    }

    private void SetUpdateAlarm()
    {
        Log.d("DebugActivity", "In SetUpdateAlarm");
        Intent updateWeatherIntent = new Intent(this, StartWeatherUpdateReceiver.class);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, updateWeatherIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        // perhaps in the future the alarm will need to pass in via the intent the location
        // to update.
        Calendar futureTime = Calendar.getInstance();
        futureTime.add(Calendar.MINUTE, 1);

        // maybe i could do repeating? no repeating is still restricted to certain values
        alarmManager.set(AlarmManager.RTC, futureTime.getTimeInMillis(), pendingIntent);

    }


    // receive the broadcast that there has been an update in the weather.
    private BroadcastReceiver testReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            Log.d("BroadcastReceiver", "Broadcast received!");
            String title = "";
            String text = "";

            // in the future we can use an on boot alarm to setup all of this
            if(intent.getBooleanExtra(ExtraConstants.UPDATE_SUCCESS, false))
            {
                // update the notification
                String location = intent.getStringExtra(ExtraConstants.LOCATION_LOCATION);
                String country = intent.getStringExtra(ExtraConstants.LOCATION_COUNTRY);
                Double currentTemp = intent.getDoubleExtra(ExtraConstants.LOCATION_CURRENT_TEMP, -9999);
                title = String.format("%s, %s", location, country);
                text = Double.toString(currentTemp);
            }
            else
            {
                title = "Error while updating weather.";
                text = "Will retry in the future";
            }

            if(notification == null)
            {
                PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                        new Intent(context, MainActivity.class), 0);

                notification = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_stat_sun)
                        .setContentIntent(contentIntent)
                        //.setOngoing(true)
                        .setOnlyAlertOnce(true);
            }
            String contentText = text;
            //contentText = contentText.concat(" " + DateFormat.getTimeInstance().format(new Date()));

            // update notification and broadcast
            notification.setContentText(contentText)
                    .setContentTitle(title)
                    .setWhen(System.currentTimeMillis());

            mNM.notify(NOTIFICATION_ID, notification.build());

            // for recurring alarm we need to re-register since we want custom values
            // not available with the recurring alarm api
            SetUpdateAlarm();

        }
    };

    /**
     * A placeholder fragment containing a simple view.
     */
    /*
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState)
        {
            View rootView = inflater.inflate(R.layout.fragment_debug, container, false);

        }
    }*/

}
