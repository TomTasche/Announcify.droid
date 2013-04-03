package com.announcify.ui.activity;

import org.mailboxer.saymyname.R;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;

import com.announcify.api.background.util.AnnouncifySettings;
import com.announcify.api.background.util.PluginSettings;
import com.announcify.api.ui.activity.PluginActivity;

public class SettingsActivity extends PluginActivity {

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, new PluginSettings(this,
				AnnouncifySettings.PREFERENCES_NAME) {

			@Override
			public String getSettingsAction() {
				return AnnouncifySettings.PREFERENCES_NAME;
			}

			@Override
			public int getPriority() {
				return 9;
			}

			@Override
			public String getEventType() {
				return "Announcify++";
			}
		}, R.xml.preferences_main_settings);

		getSupportActionBar().setTitle(R.string.preferences);
		getSupportActionBar().setSubtitle("Announcify++");

		getPreferenceScreen().findPreference("preference_more_voices")
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {

					public boolean onPreferenceClick(final Preference preference) {
						startActivity(new Intent(Intent.ACTION_VIEW, Uri
								.parse("http://blog.announcify.com/voices")));

						return false;
					}
				});

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

		getPreferenceScreen().findPreference("preference_tts_settings")
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {

					public boolean onPreferenceClick(final Preference preference) {
						final Intent intent = new Intent();
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
							intent.setAction("com.android.settings.TTS_SETTINGS");
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						} else {
							intent.setComponent(new ComponentName(
									"com.android.settings",
									"com.android.settings.TextToSpeechSettings"));
						}
						startActivity(intent);

						return false;
					}
				});

		// ugly fix for bug #4611
		// https://code.google.com/p/android/issues/detail?id=4611
		for (int i = 1; i < 6; i++) {
			applyThemeProtection("screen" + i);
		}

		setCustomListeners();
	}
}
