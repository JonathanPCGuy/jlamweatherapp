package com.hp.jlam.practice.ui.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import com.hp.jlam.practice.R;


/**
 * Created by lamjon on 7/2/2014.
 */
public class SettingsFragment extends PreferenceFragment
{
    @Override
    public void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);

        addPreferencesFromResource(R.xml.preferences);
    }

}
