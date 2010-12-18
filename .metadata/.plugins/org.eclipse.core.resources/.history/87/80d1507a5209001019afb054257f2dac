package com.announcify.plugin.mail.service;

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

import com.announcify.plugin.mail.util.Settings;

public class MailService extends Service {
	private LinkedList<MailObserver> observers;
	private LinkedList<String> addresses;
	private LinkedList<HandlerThread> threads;

	private Settings settings;

	@Override
	public void onCreate() {
		observers = new LinkedList<MailService.MailObserver>();
		addresses = new LinkedList<String>();
		threads = new LinkedList<HandlerThread>();

		settings = new Settings(this);

		final String temp = settings.getAddress();
		if (temp.length() == 0) {
			final AccountManager manager = (AccountManager) getSystemService(Context.ACCOUNT_SERVICE);
			final Account[] accounts = manager.getAccountsByType("com.google");

			if (accounts.length == 0) {
				stopSelf();
			}

			for (final Account account : accounts) {
				spawnNewObserver(account.name);
			}
		} else if (temp.contains(";")) {
			for (final String s : temp.split(";")) {
				spawnNewObserver(s);
			}
		} else {
			spawnNewObserver(temp);
		}
	}

	synchronized private void spawnNewObserver(final String address) {
		if ("".equals(address)) {
			return;
		}

		addresses.add(address);
		threads.add(new HandlerThread("MailThread for " + address));
		threads.getLast().start();
		observers.add(new MailObserver(new Handler(threads.getLast().getLooper()), address));

		getContentResolver().registerContentObserver(Uri.parse("content://gmail-ls/conversations/" + Uri.encode(address)), true, observers.getLast());
	}

	@Override
	public void onDestroy() {
		for (final MailObserver observer : observers) {
			getContentResolver().unregisterContentObserver(observer);
		}

		for (final HandlerThread thread : threads) {
			thread.getLooper().quit();
		}

		super.onDestroy();
	}

	private class MailObserver extends ContentObserver {
		private final String address;

		public MailObserver(final Handler handler, final String address) {
			super(handler);

			this.address = address;
		}

		@Override
		public void onChange(final boolean selfChange) {
			final Cursor conversations = getContentResolver().query(Uri.parse("content://gmail-ls/conversations/" + Uri.encode(address)), null, null, null, null);
			conversations.moveToFirst();

			final long conversationId = Long.valueOf(conversations.getString(conversations.getColumnIndex("conversation_id")));

			// for (String s : conversations.getColumnNames()) {
			// try {
			// Log.e("smn", ":" + s);
			// Log.e("smn",
			// conversations.getString(conversations.getColumnIndex(s)));
			// } catch (Exception e) {
			// }
			// }
			//
			// Log.e("smn", "------------");

			final Cursor messages = getContentResolver().query(Uri.parse("content://gmail-ls/conversations/" + address + "/" + Uri.parse(String.valueOf(conversationId)) + "/messages"), null, null, null, null);
			messages.moveToLast();

			// for (String s : messages.getColumnNames()) {
			// try {
			// Log.e("smn", ":" + s);
			// Log.e("smn", messages.getString(messages.getColumnIndex(s)));
			// } catch (Exception e) {
			// }
			// }

			if (!settings.getReadOwn() && address.equals(messages.getString(messages.getColumnIndex("fromAddress")))) {
				return;
			}

			final Intent intent = new Intent(MailService.this, WorkerService.class);
			intent.putExtra(WorkerService.EXTRA_FROM, messages.getString(messages.getColumnIndex("fromAddress")));
			intent.putExtra(WorkerService.EXTRA_SUBJECT, conversations.getString(conversations.getColumnIndex("subject")));
			intent.putExtra(WorkerService.EXTRA_SNIPPET, conversations.getString(conversations.getColumnIndex("snippet")));

			messages.close();
			conversations.close();
		}
	}

	@Override
	public IBinder onBind(final Intent intent) {
		return null;
	}
}