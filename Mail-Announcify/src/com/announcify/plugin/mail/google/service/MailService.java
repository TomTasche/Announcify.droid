package com.announcify.plugin.mail.google.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MailService extends Service {

	@Override
	public IBinder onBind(final Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		stopSelf();
	}
}