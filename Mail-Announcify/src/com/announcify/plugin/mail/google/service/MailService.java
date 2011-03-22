package com.announcify.plugin.mail.google.service;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

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

        private boolean paused;
        private long maxMessageIdSeen;

        private final Handler handler;

        public MailObserver(final Handler handler) {
            super(handler);

            this.handler = handler;

            final String[] projection = new String[] { "maxMessageId" };
            final Cursor cursor = getContentResolver().query(Uri.parse("content://gmail-ls/unread/"), projection, null, null, null);

            try {
                if (!cursor.moveToFirst()) {
                    return;
                }

                maxMessageIdSeen = Long.valueOf(cursor.getString(cursor.getColumnIndex(projection[0])));
            } finally {
                cursor.close();
            }
        }

        @Override
        public void onChange(final boolean selfChange) {
            if (paused) {
                return;
            }

            Cursor conversations = null;
            Cursor messages = null;

            try {
                String[] projection = new String[] { "conversation_id", "maxMessageId" };
                conversations = getContentResolver().query(
                        // Uri.parse("content://gmail-ls/conversations/"
                        // + address), projection,
                        Uri.parse("content://gmail-ls/unread/"), projection, null, null, null);
                if (!conversations.moveToFirst()) {
                    return;
                }

//                final long conversationId = Long.valueOf(conversations.getString(conversations.getColumnIndex(projection[0])));
//
//                final long maxMessageId = Long.valueOf(conversations.getString(conversations.getColumnIndex(projection[1])));
//                if (maxMessageId < maxMessageIdSeen) {
//                    return;
//                }
//                maxMessageIdSeen = maxMessageId;
//
//                projection = new String[] { "fromAddress", "subject", "snippet", "body" };
//
//                messages = getContentResolver().query(Uri.parse("content://gmail-ls/conversations/" + address + "/" + Uri.parse(String.valueOf(conversationId)) + "/messages"), projection, null, null, null);
//                if (!messages.moveToLast()) {
//                    return;
//                }
//
//                if (!settings.getReadOwn() && addresses.contains(messages.getString(messages.getColumnIndex(projection[0])))) {
//                    return;
//                }
//
//                final Intent intent = new Intent(MailService.this, WorkerService.class);
//                intent.setAction(PluginService.ACTION_ANNOUNCE);
//                intent.putExtra(WorkerService.EXTRA_FROM, messages.getString(messages.getColumnIndex(projection[0])));
//                intent.putExtra(WorkerService.EXTRA_SUBJECT, messages.getString(messages.getColumnIndex(projection[1])));
//                intent.putExtra(WorkerService.EXTRA_SNIPPET, messages.getString(messages.getColumnIndex(projection[2])));
//                intent.putExtra(WorkerService.EXTRA_MESSAGE, messages.getString(messages.getColumnIndex(projection[3])));
//                startService(intent);
//
//                paused = true;
//                handler.postDelayed(new Runnable() {
//
//                    public void run() {
//                        paused = false;
//                    }
//                }, 5000);
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

    private HashSet<String> addresses;

    private MailObserver observer;
    private HandlerThread thread;
    private Settings settings;

    @Override
    public IBinder onBind(final Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(getBaseContext()));

        addresses = new HashSet<String>();
        settings = new Settings(this);

        final AccountManager manager = (AccountManager) getSystemService(Context.ACCOUNT_SERVICE);
        final Account[] accounts = manager.getAccountsByType("com.google");
        for (final Account account : accounts) {
            addresses.add(account.name);
        }

        final String temp = settings.getAddress();
        if (temp.contains(";")) {
            for (final String s : temp.split(";")) {
                addresses.add(s);
            }
        } else {
            addresses.add(temp);
        }
        
        spawnNewObserver();
    }

    @Override
    public void onDestroy() {
        getContentResolver().unregisterContentObserver(observer);
        thread.getLooper().quit();
        super.onDestroy();
    }

    synchronized private void spawnNewObserver() {
        thread = new HandlerThread("MailThread");
        thread.start();

        final Handler handler = new Handler(thread.getLooper());
        handler.post(new Runnable() {

            public void run() {
                Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(getBaseContext()));
            }
        });

        observer = new MailObserver(handler);

        getContentResolver().registerContentObserver(Uri.parse("content://gmail-ls/unread/"), true, observer);
    }
}
