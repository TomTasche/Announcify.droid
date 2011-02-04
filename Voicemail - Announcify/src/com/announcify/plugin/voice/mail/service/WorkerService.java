package com.announcify.plugin.voice.mail.service;

import android.content.Intent;

import com.announcify.api.background.service.PluginService;
import com.announcify.api.simple.SimpleAnnouncifyIntent;
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
        if (settings == null) {
            settings = new Settings(this);
        }

        if (ACTION_ANNOUNCE.equals(intent.getAction())) {
            String message = intent.getStringExtra(settings.getEventType());
            if (message == null) return;

            message = "New " + message;

            final SimpleAnnouncifyIntent announcify = new SimpleAnnouncifyIntent(
                    this);
            announcify.announce(message);
        } else {
            super.onHandleIntent(intent);
        }
    }
}
