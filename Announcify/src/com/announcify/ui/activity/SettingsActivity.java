package com.announcify.ui.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.widget.Toast;

import com.announcify.R;
import com.announcify.api.background.util.AnnouncifySettings;
import com.announcify.api.ui.activity.PluginActivity;


public class SettingsActivity extends PluginActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getPreferenceManager().setSharedPreferencesName(
                AnnouncifySettings.PREFERENCES_NAME);
        getPreferenceManager().setSharedPreferencesMode(
                Context.MODE_WORLD_READABLE);
        addPreferencesFromResource(R.xml.preferences_main_settings);

        getPreferenceScreen().findPreference("preference_replace_chooser")
        .setOnPreferenceClickListener(new OnPreferenceClickListener() {

            public boolean onPreferenceClick(final Preference preference) {
                startActivity(new Intent(SettingsActivity.this,
                        ReplaceActivity.class));

                return false;
            }
        });

        getPreferenceScreen().findPreference("preference_choose_group")
        .setOnPreferenceClickListener(new OnPreferenceClickListener() {

            public boolean onPreferenceClick(final Preference preference) {
                startActivity(new Intent(SettingsActivity.this,
                        GroupActivity.class));

                return false;
            }
        });
        
        getPreferenceScreen().findPreference("preference_choose_contact")
        .setOnPreferenceClickListener(new OnPreferenceClickListener() {

            public boolean onPreferenceClick(final Preference preference) {
                startActivity(new Intent(SettingsActivity.this,
                        ContactActivity.class));

                return false;
            }
        });

        getPreferenceScreen().findPreference("preference_spam_filter")
        .setOnPreferenceClickListener(new OnPreferenceClickListener() {

            public boolean onPreferenceClick(final Preference preference) {
                Toast.makeText(SettingsActivity.this,
                        "Not yet implemented, sorry!",
                        Toast.LENGTH_LONG).show();

                return false;
            }
        });

        getPreferenceScreen().findPreference("preference_tts_settings")
        .setOnPreferenceClickListener(new OnPreferenceClickListener() {

            public boolean onPreferenceClick(final Preference preference) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.TextToSpeechSettings"));
                startActivity(intent);

                return false;
            }
        });        

        // ugly fix for bug #4611
        // https://code.google.com/p/android/issues/detail?id=4611
        for (int i = 1; i < 5; i++) {
            applyThemeProtection("screen" + i);
        }
    }
}
