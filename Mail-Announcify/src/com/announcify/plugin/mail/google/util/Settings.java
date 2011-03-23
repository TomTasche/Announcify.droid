package com.announcify.plugin.mail.google.util;

import android.content.Context;

import com.announcify.api.background.util.PluginSettings;
import com.announcify.plugin.mail.google.R;

public class Settings extends PluginSettings {

    public static final String ACTION_SETTINGS = "com.announcify.plugin.mail.google.SETTINGS";
    public static final String PREFERENCES_NAME = "com.announcify.plugin.mail.google";
    public static final String KEY_READ_OWN = "preference_read_own";
    public static final String KEY_SHUT_UP = "preference_shut_up";
    public static final String KEY_MESSAGE = "preference_read_message";

    public Settings(final Context context) {
        super(context, PREFERENCES_NAME);
    }

    @Override
    public String getEventType() {
        return context.getString(R.string.event_mail);
    }

    @Override
    public int getPriority() {
        return 5;
    }

    @Override
    public String getSettingsAction() {
        return ACTION_SETTINGS;
    }
}
