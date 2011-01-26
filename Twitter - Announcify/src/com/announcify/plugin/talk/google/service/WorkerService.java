
package com.announcify.plugin.talk.google.service;

import android.content.Intent;

import com.announcify.api.AnnouncifyIntent;
import com.announcify.api.contact.Contact;
import com.announcify.api.error.ExceptionHandler;
import com.announcify.api.service.PluginService;
import com.announcify.api.text.Formatter;
import com.announcify.plugin.talk.google.contact.Chat;
import com.announcify.plugin.talk.google.receiver.RingtoneReceiver;
import com.announcify.plugin.talk.google.util.Settings;

public class WorkerService extends PluginService {
    public static final String EXTRA_FROM = "com.announcify.plugin.talk.EXTRA_FROM";

    public static final String EXTRA_MESSAGE = "com.announcify.plugin.talk.EXTRA_MESSAGE";

    public WorkerService() {
        super("Announcify - Talk");
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this, Thread
                .getDefaultUncaughtExceptionHandler()));

        final String address = intent.getStringExtra(EXTRA_FROM);

        final Settings settings = new Settings(this);

        if (address == null && "".equals(address)) {
            return;
        }
        final Contact contact = new Contact(this, new Chat(this), address);

        final Formatter formatter = new Formatter(this, contact, settings);

        final AnnouncifyIntent announcify = new AnnouncifyIntent(this, settings);
        announcify.setStopBroadcast(RingtoneReceiver.ACTION_START_RINGTONE);
        announcify.announce(formatter.format(intent.getStringExtra(EXTRA_MESSAGE)));
    }
}
