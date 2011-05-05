package com.announcify.plugin.twitter.plume.service;

import android.content.Intent;
import android.os.Handler;

import com.announcify.api.AnnouncifyIntent;
import com.announcify.api.background.contact.Contact;
import com.announcify.api.background.contact.ContactFilter;
import com.announcify.api.background.contact.lookup.Twitter;
import com.announcify.api.background.error.ExceptionHandler;
import com.announcify.api.background.service.PluginService;
import com.announcify.api.background.text.Formatter;
import com.announcify.plugin.twitter.plume.util.Settings;

public class WorkerService extends PluginService {

    public final static String ACTION_START_RINGTONE = "com.announcify.plugin.twitter.plume.ACTION_START_RINGTONE";
    public final static String ACTION_STOP_RINGTONE = "com.announcify.plugin.twitter.plume.ACTION_STOP_RINGTONE";

    private boolean paused;

    public WorkerService() {
        super("Announcify - Plume", ACTION_START_RINGTONE, ACTION_STOP_RINGTONE);
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
	// TODO: use ContentProvider?
	// content://com.levelup.touiteur.provider/timeline/ - content://com.levelup.touiteur.provider/mentions/ - content://com.levelup.touiteur.provider/messages/

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(getBaseContext()));

        if (settings == null) {
            settings = new Settings(this);
        }

        if (ACTION_ANNOUNCE.equals(intent.getAction())) {
            if (paused) {
                return;
            }
            
            if (intent.getIntExtra("UnreadM", 0) < 1 && intent.getIntExtra("UnreadD", 0) < 1) return;

            final String message = intent.getStringExtra("UnreadText");
            String number = intent.getStringExtra("User");

            final Settings settings = new Settings(this);

            if (number == null) {
                number = "";
            }
            final Contact contact = new Contact(this, new Twitter(this), number);

            if (contact.getLookupString() == null || contact.getLookupString().equals("")) {
                contact.setFullname(number);
                contact.setLookupString("com.announcify.CHEAT");
            }

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
