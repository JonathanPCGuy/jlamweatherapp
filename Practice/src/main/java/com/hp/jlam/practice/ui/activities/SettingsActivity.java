package com.hp.jlam.practice.ui.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.hp.jlam.practice.ui.fragments.SettingsFragment;

/**
 * Created by lamjon on 7/2/2014.
 */
public class SettingsActivity extends PreferenceActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // display the fragment as the main content
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
