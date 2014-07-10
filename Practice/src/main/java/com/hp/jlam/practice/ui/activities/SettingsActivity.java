package com.hp.jlam.practice.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

import com.hp.jlam.practice.R;
import com.hp.jlam.practice.receivers.StartWeatherUpdateReceiver;
import com.hp.jlam.practice.ui.fragments.SettingsFragment;

public class SettingsActivity extends PreferenceActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener
{
    public final static String TAG = "SettingsActivity";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        // display the fragment as the main content
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();

        // register for pref changes so we can update background stuff

        // should have picked the "default" shared pref for my prefs, oh well
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected  void onPause()
    {
        Log.d(TAG, "onPause");
        super.onPause();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        Log.d(TAG, "onSharedPreferenceChanged event received");
        if(key.equals(getString(R.string.pref_key_weather_update_interval)))
        {
            // trigger update now. let the setting of the alarm pick up the new setting
            // later on we may want to be smart about this but for now this lets us avoid
            // having to modify an existing alarm to inc/dec the waiting period
            // might need to do this in the future to prevent constant changing of prefs
            // triggering multiple update jobs
            //int updateInterval = WeatherPrefs.GetWeatherUpdateInterval(this);
            // update the alarm
            // same intent service will process two different sets of options
            // so need to account for that
            Log.d(TAG, "Broadcasting to StartUpdateWeatherReceiver");
            Intent serviceIntent = new Intent(this, StartWeatherUpdateReceiver.class);
            sendBroadcast(serviceIntent);
        }
    }
}
