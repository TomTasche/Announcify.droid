package com.announcify.plugin.mail.google.service;

import java.util.LinkedList;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;

import com.announcify.api.background.error.ExceptionHandler;
import com.announcify.api.background.service.PluginService;
import com.announcify.plugin.mail.google.util.Settings;

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