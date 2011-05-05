package com.announcify.developers.activity;

import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceClickListener;

import com.announcify.api.ui.activity.PluginActivity;
import com.announcify.developers.sample.R;
import com.announcify.developers.util.Settings;

public class SettingsActivity extends PreferenceActivity {

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        // fill the activity with basic settings
        super.onCreate(savedInstanceState);

        // if you need some more advanced settings, do this:
        // addPreferencesFromResource(R.xml.preferences_extra_settings);
        // setExtraCustomListeners();
        
        addPreferencesFromResource(R.xml.preferences_more_settings);
        
        findPreference("preference_announce").setOnPreferenceClickListener(new OnPreferenceClickListener() {
            
            public boolean onPreferenceClick(Preference preference) {
                
                
                return false;
            }
        });
    }
}
