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

        private boolean paused;
        private long maxMessageIdSeen;

        private final String address;
        private final Handler handler;

        public MailObserver(final Handler handler, final String address) {
            super(handler);

            this.handler = handler;
            this.address = Uri.encode(address);

            final String[] projection = new String[] { "maxMessageId" };
            final Cursor cursor = getContentResolver().query(Uri.parse("content://gmail-ls/conversations/" + address), projection, null, null, null);

            try {
                if (cursor == null || !cursor.moveToFirst()) {
                    return;
                }

                maxMessageIdSeen = Long.valueOf(cursor.getString(cursor.getColumnIndex(projection[0])));
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }

        @Override
        public void onChange(final boolean selfChange) {
            if (paused) {
                return;
            }

            Cursor unread = null;
            Cursor conversations = null;
            Cursor messages = null;

            try {
                String[] projection = new String[] { "name", "numUnreadConversations" };
                unread = getContentResolver().query(Uri.parse("content://gmail-ls/labels/" + address), projection, null, null, null);
                if (unread == null || !unread.moveToFirst()) {
                    return;
                }

                // WHERE clause doesn't seem to work, so we have to iterate
                // through the whole cursor
                final int nameId = unread.getColumnIndex(projection[0]);
                do {
                    // final String label = settings.isAnnoyingMode() ? "^u" :
                    // "^i";
                    final String label = "^i";
                    if (label.equals(unread.getString(nameId))) {
                        if (unread.getInt(unread.getColumnIndex(projection[1])) <= 0) {
                            return;
                        } else {
                            break;
                        }
                    }
                } while (unread.moveToNext());

                projection = new String[] { "conversation_id", "maxMessageId" };
                conversations = getContentResolver().query(Uri.parse("content://gmail-ls/conversations/" + address), projection, null, null, null);
                if (conversations == null || !conversations.moveToFirst()) {
                    return;
                }

                final long conversationId = Long.valueOf(conversations.getString(conversations.getColumnIndex(projection[0])));

                final long maxMessageId = Long.valueOf(conversations.getString(conversations.getColumnIndex(projection[1])));
                if (maxMessageId <= maxMessageIdSeen) {
                    return;
                }
                maxMessageIdSeen = maxMessageId;

                projection = new String[] { "fromAddress", "subject", "snippet", "body" };
                messages = getContentResolver().query(Uri.parse("content://gmail-ls/conversations/" + address + "/" + Uri.parse(String.valueOf(conversationId)) + "/messages"), projection, null, null, null);
                if (messages == null || !messages.moveToLast()) {
                    return;
                }

                final String username = messages.getString(messages.getColumnIndex(projection[0]));
                if (!settings.getReadOwn()) {
                    if (addresses.contains(prepareAddress(username))) {
                        return;
                    }
                }

                final Intent intent = new Intent(MailService.this, WorkerService.class);
                intent.setAction(PluginService.ACTION_ANNOUNCE);
                intent.putExtra(WorkerService.EXTRA_FROM, username);
                intent.putExtra(WorkerService.EXTRA_SUBJECT, messages.getString(messages.getColumnIndex(projection[1])));
                intent.putExtra(WorkerService.EXTRA_SNIPPET, messages.getString(messages.getColumnIndex(projection[2])));
                intent.putExtra(WorkerService.EXTRA_MESSAGE, messages.getString(messages.getColumnIndex(projection[3])));
                startService(intent);

                paused = true;
                handler.postDelayed(new Runnable() {

                    public void run() {
                        paused = false;
                    }
                }, settings.getShutUp());
            } finally {
                if (messages != null) {
                    messages.close();
                }
                if (conversations != null) {
                    conversations.close();
                }
                if (unread != null) {
                    unread.close();
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
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(getBaseContext()));

        observers = new LinkedList<MailService.MailObserver>();
        addresses = new LinkedList<String>();
        threads = new LinkedList<HandlerThread>();

        settings = new Settings(this);

        final AccountManager manager = (AccountManager) getSystemService(Context.ACCOUNT_SERVICE);
        final Account[] accounts = manager.getAccountsByType("com.google");

        if (accounts.length == 0) {
            stopSelf();
        }

        for (final Account account : accounts) {
            spawnNewObserver(account.name);
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

    private void spawnNewObserver(final String address) {
        if (address == null || "".equals(address)) {
            return;
        }

        addresses.add(address);
        threads.add(new HandlerThread("MailThread for " + address));
        threads.getLast().start();

        final Handler handler = new Handler(threads.getLast().getLooper());
        handler.post(new Runnable() {

            public void run() {
                Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(getBaseContext()));
            }
        });

        observers.add(new MailObserver(handler, address));

        getContentResolver().registerContentObserver(Uri.parse("content://gmail-ls/conversations/" + Uri.encode(address)), true, observers.getLast());
    }

    public static String prepareAddress(String address) {
        if (address != null && address.contains("<") && address.contains(">")) {
            address = address.substring(address.indexOf('<') + 1, address.indexOf('>'));
        }

        return address;
    }
}