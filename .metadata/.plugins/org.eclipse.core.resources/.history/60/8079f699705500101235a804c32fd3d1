package com.announcify.plugin.voice.mail.service;

import android.content.Intent;

import com.announcify.api.AnnouncifyIntent;
import com.announcify.api.background.error.ExceptionHandler;
import com.announcify.api.background.service.PluginService;
import com.announcify.plugin.voice.mail.util.Settings;

public class WorkerService extends PluginService {

    public static final String ACTION_START_RINGTONE = "com.announcify.plugin.voice.mail.ACTION_START_RINGTONE";
    public static final String ACTION_STOP_RINGTONE = "com.announcify.plugin.voice.mail.ACTION_STOP_RINGTONE";

    public WorkerService() {
        super("Announcify - Voicemail", ACTION_START_RINGTONE,
                ACTION_STOP_RINGTONE);
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(
                getBaseContext()));

        if (settings == null) {
            settings = new Settings(this);
        }

        if (ACTION_ANNOUNCE.equals(intent.getAction())) {
            final String message = "New "
                    + intent.getStringExtra(settings.getEventType());

            final AnnouncifyIntent announcify = new AnnouncifyIntent(this,
                    settings);
            announcify.setStopBroadcast(ACTION_START_RINGTONE);
            announcify.announce(message);
        } else {
            super.onHandleIntent(intent);

            startService(new Intent(this, VoicemailService.class));
        }

        stopSelf();
    }
}
