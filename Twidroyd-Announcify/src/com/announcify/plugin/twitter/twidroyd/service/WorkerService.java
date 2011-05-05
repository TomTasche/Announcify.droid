package com.announcify.plugin.twitter.twidroyd.service;

import android.content.Intent;
import android.os.Handler;

import com.announcify.api.AnnouncifyIntent;
import com.announcify.api.background.contact.Contact;
import com.announcify.api.background.contact.ContactFilter;
import com.announcify.api.background.contact.lookup.Twitter;
import com.announcify.api.background.error.ExceptionHandler;
import com.announcify.api.background.service.PluginService;
import com.announcify.api.background.text.Formatter;
import com.announcify.plugin.twitter.twidroyd.util.Settings;

public class WorkerService extends PluginService {

    public final static String ACTION_START_RINGTONE = "com.announcify.plugin.twitter.twidroyd.ACTION_START_RINGTONE";
    public final static String ACTION_STOP_RINGTONE = "com.announcify.plugin.twitter.twidroyd.ACTION_STOP_RINGTONE";

    private boolean paused;

    public WorkerService() {
        super("Announcify - Twidroyd", ACTION_START_RINGTONE, ACTION_STOP_RINGTONE);
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(getBaseContext()));

        if (settings == null) {
            settings = new Settings(this);
        }

        if (ACTION_ANNOUNCE.equals(intent.getAction())) {
            if (paused) {
                return;
            }

            final String message = "New post at Twitter";
            String number = "";

            final Settings settings = new Settings(this);

            if (number == null) {
                number = "";
            }
            final Contact contact = new Contact(this, new Twitter(this), number);

            if (!settings.isChuckNorris()) {
                if (!ContactFilter.announcableContact(this, contact)) {
                    playRingtone();
                    return;
                }
            }

            final Formatter formatter = new Formatter(this, contact, settings);

            final AnnouncifyIntent announcify = new AnnouncifyIntent(this, settings);
            announcify.setStopBroadcast(ACTION_START_RINGTONE);
            announcify.announce(formatter.format(message));

            paused = true;
            new Handler().postDelayed(new Runnable() {

                public void run() {
                    paused = false;
                }
            }, settings.getShutUp());
        } else {
            super.onHandleIntent(intent);
        }
    }
}
