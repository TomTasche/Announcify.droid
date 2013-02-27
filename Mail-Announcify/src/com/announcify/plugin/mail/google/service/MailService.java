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
import com.google.android.gm.contentprovider.GmailContract;

public class MailService extends Service {

	private class MailObserver extends ContentObserver {

		private boolean paused;

		private final Handler handler;

		public MailObserver(final Handler handler) {
			super(handler);

			this.handler = handler;
		}

		@Override
		public void onChange(final boolean selfChange) {
			if (paused) {
				return;
			}

			int unreadMails = 0;
			for (String address : addresses) {
				Cursor labelsCursor = null;
				try {
					labelsCursor = getContentResolver().query(
							GmailContract.Labels.getLabelsUri(address), null,
							null, null, null);

					if (labelsCursor != null) {
						final int canonicalNameIndex = labelsCursor
								.getColumnIndexOrThrow(GmailContract.Labels.CANONICAL_NAME);
						while (labelsCursor.moveToNext()) {
							if (GmailContract.Labels.LabelCanonicalNames.CANONICAL_NAME_INBOX
									.equals(labelsCursor
											.getString(canonicalNameIndex))) {
								final Uri inboxUri = Uri
										.parse(labelsCursor.getString(labelsCursor
												.getColumnIndex(GmailContract.Labels.URI)));

								Cursor inboxCursor = null;
								try {
									inboxCursor = getContentResolver().query(
											inboxUri, null, null, null, null);
									if (inboxCursor.moveToFirst()) {
										unreadMails += inboxCursor
												.getInt(inboxCursor
														.getColumnIndex(GmailContract.Labels.NUM_UNREAD_CONVERSATIONS));
									}
								} finally {
									if (inboxCursor != null)
										inboxCursor.close();
								}
							}
						}
					}
				} finally {
					if (labelsCursor != null)
						labelsCursor.close();
				}
			}

			if (unreadMails <= 0)
				return;

			final Intent intent = new Intent(MailService.this,
					WorkerService.class);
			intent.setAction(PluginService.ACTION_ANNOUNCE);
			intent.putExtra(WorkerService.EXTRA_MESSAGE, unreadMails
					+ " unread mails");
			startService(intent);

			paused = true;
			handler.postDelayed(new Runnable() {

				public void run() {
					paused = false;
				}
			}, settings.getShutUp());
		}
	}

	private LinkedList<String> addresses;

	private HandlerThread thread;
	private MailObserver observer;
	private Settings settings;

	@Override
	public IBinder onBind(final Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(
				getBaseContext()));

		thread = new HandlerThread("TalkThread");
		thread.start();

		settings = new Settings(this);

		final Handler handler = new Handler(thread.getLooper());
		handler.post(new Runnable() {

			public void run() {
				Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(
						getBaseContext()));
			}
		});

		addresses = new LinkedList<String>();

		final AccountManager manager = (AccountManager) getSystemService(Context.ACCOUNT_SERVICE);
		final Account[] accounts = manager.getAccountsByType("com.google");

		if (accounts.length == 0) {
			stopSelf();
		}

		observer = new MailObserver(handler);
		for (Account account : accounts) {
			String address = account.name;
			addresses.add(address);

			getContentResolver().registerContentObserver(
					GmailContract.Labels.getLabelsUri(address), true, observer);
		}
	}

	@Override
	public void onDestroy() {
		getContentResolver().unregisterContentObserver(observer);

		thread.getLooper().quit();

		super.onDestroy();
	}
}