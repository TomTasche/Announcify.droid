package com.announcify.plugin.mail.activity;

import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;

import com.announcify.activity.AnnouncifyActivity;
import com.announcify.plugin.mail.R;
import com.announcify.plugin.mail.service.MailService;

public class Mailnouncify extends AnnouncifyActivity {
	public static final String ACTION_SETTINGS = "com.announcify.plugin.mail.SETTINGS";

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getPreferenceManager().setSharedPreferencesName(ACTION_SETTINGS);
		getPreferenceManager().setSharedPreferencesMode(Context.MODE_WORLD_READABLE);
		addPreferencesFromResource(R.xml.preferences_settings);
	}

	@Override
	protected void onPause() {
		final Intent serviceIntent = new Intent(this, MailService.class);
		stopService(serviceIntent);
		startService(serviceIntent);
		super.onPause();
	}

	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		parseRingtone(requestCode, resultCode, data, RingtoneManager.TYPE_NOTIFICATION);

		super.onActivityResult(requestCode, resultCode, data);
	}
}