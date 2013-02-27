package com.announcify.plugin.mail.google.service;

import android.content.Intent;

import com.announcify.api.AnnouncifyIntent;
import com.announcify.api.background.error.ExceptionHandler;
import com.announcify.api.background.service.PluginService;
import com.announcify.plugin.mail.google.util.Settings;

public class WorkerService extends PluginService {

	public static final String ACTION_START_RINGTONE = "com.announcify.plugin.mail.google.ACTION_START_RINGTONE";
	public static final String ACTION_STOP_RINGTONE = "com.announcify.plugin.mail.google.ACTION_STOP_RINGTONE";
	public static final String EXTRA_MESSAGE = "com.announcify.plugin.mail.google.EXTRA_MESSAGE";

	public WorkerService() {
		super("Announcify - Mail", ACTION_START_RINGTONE, ACTION_STOP_RINGTONE);
	}

	@Override
	protected void onHandleIntent(final Intent intent) {
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(
				getBaseContext()));

		if (settings == null) {
			settings = new Settings(this);
		}

		if (ACTION_ANNOUNCE.equals(intent.getAction())) {
			String message = intent.getStringExtra(EXTRA_MESSAGE);

			final AnnouncifyIntent announcify = new AnnouncifyIntent(this,
					settings);
			announcify.setStopBroadcast(ACTION_START_RINGTONE);
			announcify.announce(message);
		} else {
			super.onHandleIntent(intent);
		}
	}
}
