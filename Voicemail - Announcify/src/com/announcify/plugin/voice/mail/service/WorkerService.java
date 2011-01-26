
package com.announcify.plugin.voice.mail.service;

import android.content.Intent;

import com.announcify.api.AnnouncifyIntent;
import com.announcify.api.contact.Contact;
import com.announcify.api.error.ExceptionHandler;
import com.announcify.api.queue.PluginQueue;
import com.announcify.api.service.PluginService;
import com.announcify.api.text.Formatter;
import com.announcify.plugin.voice.mail.receiver.RingtoneReceiver;
import com.announcify.plugin.voice.mail.util.Settings;

public class WorkerService extends PluginService {

    public WorkerService() {
        super("Announcify - Voicemail");
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this, Thread
                .getDefaultUncaughtExceptionHandler()));

        final PluginQueue queue;

        final Settings settings = new Settings(this);

        final Formatter formatter = new Formatter(this, new Contact(), settings);

        final AnnouncifyIntent announcify = new AnnouncifyIntent(this, settings);
        announcify.setStopBroadcast(RingtoneReceiver.ACTION_START_RINGTONE);
        announcify
                .announce(formatter.format(intent.getStringExtra("New " + settings.getEventType())));
    }
}
