package com.announcify.plugin.voice.call.activity;

import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;

import com.announcify.api.ui.activity.PluginActivity;
import com.announcify.plugin.voice.call.R;
import com.announcify.plugin.voice.call.util.Settings;

public class SettingsActivity extends PluginActivity {

	@Override
	protected void onActivityResult(final int requestCode,
			final int resultCode, final Intent data) {
		parseRingtone(requestCode, resultCode, data,
				RingtoneManager.TYPE_RINGTONE);

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, new Settings(this),
				R.xml.preferences_settings_call);
		
		getSupportActionBar().setSubtitle(R.string.event_call);
	}
}
