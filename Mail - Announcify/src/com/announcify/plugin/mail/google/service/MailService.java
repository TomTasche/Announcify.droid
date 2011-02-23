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

    private class MailObserver extends ContentObserver {

        private final String address;

        private long maxMessageIdSeen;

        public MailObserver(final Handler handler, final String address) {
            super(handler);

            this.address = Uri.encode(address);

            final String[] projection = new String[] { "maxMessageId" };
            final Cursor cursor = getContentResolver().query(
                    Uri.parse("content://gmail-ls/conversations/"
                            + Uri.encode(address)), projection, null, null,
                    null);
            cursor.moveToFirst();

            maxMessageIdSeen = Long.valueOf(cursor.getString(cursor
                    .getColumnIndex(projection[0])));

            cursor.close();
        }

        @Override
        public void onChange(final boolean selfChange) {
            Cursor conversations = null;
            Cursor messages = null;

            try {
                String[] projection = new String[] { "conversation_id",
                        "maxMessageId" };
                conversations = getContentResolver().query(
                        Uri.parse("content://gmail-ls/conversations/"
                                + Uri.encode(address)), projection,
                        projection[1] + " > " + maxMessageIdSeen, null, null);
                if (!conversations.moveToFirst()) return;

                final long conversationId = Long
                        .valueOf(conversations.getString(conversations
                                .getColumnIndex(projection[0])));

                maxMessageIdSeen = Long
                        .valueOf(conversations.getString(conversations
                                .getColumnIndex(projection[1])));

                projection = new String[] { "fromAddress", "subject",
                        "snippet", "body" };

                messages = getContentResolver().query(
                        Uri.parse("content://gmail-ls/conversations/"
                                + Uri.encode(address) + "/"
                                + Uri.parse(String.valueOf(conversationId))
                                + "/messages"), projection, null, null, null);
                if (messages.moveToLast()) return;

                if (!settings.getReadOwn()
                        && address.equals(messages.getString(messages
                                .getColumnIndex(projection[0])))) return;

                final Intent intent = new Intent(MailService.this,
                        WorkerService.class);
                intent.setAction(PluginService.ACTION_ANNOUNCE);
                intent.putExtra(WorkerService.EXTRA_FROM, messages
                        .getString(messages.getColumnIndex(projection[0])));
                intent.putExtra(WorkerService.EXTRA_SUBJECT, messages
                        .getString(messages.getColumnIndex(projection[1])));
                intent.putExtra(WorkerService.EXTRA_SNIPPET, messages
                        .getString(messages.getColumnIndex(projection[2])));
                intent.putExtra(WorkerService.EXTRA_MESSAGE, messages
                        .getString(messages.getColumnIndex(projection[3])));
                startService(intent);
            } finally {
                if (messages != null) {
                    messages.close();
                }
                if (conversations != null) {
                    conversations.close();
                }
            }
        }
    }

    private LinkedList<MailObserver> observers;

    private LinkedList<String> addresses;

    private LinkedList<HandlerThread> threads;

    private Settings settings;

    @Override
    public IBinder onBind(final Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        
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

    synchronized private void spawnNewObserver(final String address) {
        if ("".equals(address)) return;

        addresses.add(address);
        threads.add(new HandlerThread("MailThread for " + address));
        threads.getLast().start();
        
        Handler handler = new Handler(threads.getLast().getLooper());
        handler.post(new Runnable() {
            
            public void run() {
                Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(MailService.this));
            }
        });
        
        observers.add(new MailObserver(handler, address));

        getContentResolver().registerContentObserver(
                Uri.parse("content://gmail-ls/conversations/"
                        + Uri.encode(address)), true, observers.getLast());
    }
}
