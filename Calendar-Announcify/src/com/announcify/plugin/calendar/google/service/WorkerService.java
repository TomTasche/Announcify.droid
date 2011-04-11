
package com.announcify.plugin.calendar.google.service;

import android.content.Intent;

import com.announcify.api.AnnouncifyIntent;
import com.announcify.api.contact.Contact;
import com.announcify.api.error.ExceptionHandler;
import com.announcify.api.service.PluginService;
import com.announcify.api.text.Formatter;
import com.announcify.plugin.calendar.google.util.Settings;

public class WorkerService extends PluginService {

    public WorkerService() {
        super("Announcify - Calendar");
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this, Thread
                .getDefaultUncaughtExceptionHandler()));

        final Settings settings = new Settings(this);

        final Formatter formatter = new Formatter(this, new Contact(), settings);

        final String appointment = null;

        // leads to:
        // 1. no announcement if user doesn't want to hear a message
        // 2. support for discreet
        final AnnouncifyIntent announcify = new AnnouncifyIntent(this, settings);
        announcify.announce(formatter.format(appointment));
    }
}
