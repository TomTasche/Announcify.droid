package com.announcify.plugin.mail.k9.service;

import android.app.Service;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;

import com.announcify.api.background.error.ExceptionHandler;
import com.announcify.api.background.service.PluginService;


public class MailService extends Service {

    private class MailObserver extends ContentObserver {

        private boolean paused;
        private long maxMessageIdSeen;
        
        private Handler handler;

        public MailObserver(final Handler handler) {
            super(handler);
            
            this.handler = handler;

            final String[] projection = new String[] { "_id" };
            final Cursor cursor = getContentResolver().query(Uri.parse("content://com.fsck.k9.messageprovider/inbox_messages/"), projection, null, null, null);

            try {
                if (!cursor.moveToFirst()) return;

                maxMessageIdSeen = Long.valueOf(cursor.getString(cursor.getColumnIndex(projection[0])));
            } finally {
                cursor.close();
            }
        }

        @Override
        public void onChange(final boolean selfChange) {
            if (paused) return;
            
            Cursor conversations = null;

            try {
                String[] projection = new String[] { "_id", "sender", "subject", "preview" };
                conversations = getContentResolver().query(
                        Uri.parse("content://com.fsck.k9.messageprovider/inbox_messages/"), projection,
                        null, null, null);
                if (!conversations.moveToFirst()) return;

                long maxMessageId = Long
                .valueOf(conversations.getString(conversations
                        .getColumnIndex(projection[0])));

                if (maxMessageId < maxMessageIdSeen) return;
                maxMessageIdSeen = maxMessageId;

                final Intent intent = new Intent(MailService.this,
                        WorkerService.class);
                intent.setAction(PluginService.ACTION_ANNOUNCE);
                intent.putExtra(WorkerService.EXTRA_FROM, conversations
                        .getString(conversations.getColumnIndex(projection[0])));
                intent.putExtra(WorkerService.EXTRA_SUBJECT, conversations
                        .getString(conversations.getColumnIndex(projection[1])));
                intent.putExtra(WorkerService.EXTRA_MESSAGE, conversations
                        .getString(conversations.getColumnIndex(projection[3])));
                startService(intent);
                
                paused = true;
                handler.postDelayed(new Runnable() {
                    
                    public void run() {
                        paused = false;
                    }
                }, 5000);
            } finally {
                if (conversations != null) {
                    conversations.close();
                }
            }
        }
    }

    private MailObserver observer;
    private HandlerThread thread;

    @Override
    public IBinder onBind(final Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(getBaseContext()));

        thread = new HandlerThread("HandlerThread for K9Mail");
        thread.start();
        
        Handler handler = new Handler(thread.getLooper());
        handler.post(new Runnable() {

            public void run() {
                Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(getBaseContext()));
            }
        });
        
        observer = new MailObserver(handler);
        
        getContentResolver().registerContentObserver(Uri.parse("content://com.fsck.k9.messageprovider/inbox_messages/"), true, observer);
    }

    @Override
    public void onDestroy() {
        getContentResolver().unregisterContentObserver(observer);

        thread.getLooper().quit();

        super.onDestroy();
    }
}
