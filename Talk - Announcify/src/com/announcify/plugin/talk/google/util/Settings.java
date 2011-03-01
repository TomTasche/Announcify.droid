package com.announcify.plugin.talk.google.util;

import android.content.Context;

import com.announcify.api.background.util.PluginSettings;
import com.announcify.plugin.talk.google.R;


public class Settings extends PluginSettings {

    public static final String ACTION_SETTINGS = "com.announcify.plugin.talk.google.SETTINGS";

    public static final String PREFERENCES_NAME = "com.announcify.plugin.talk.google";

    public Settings(final Context context) {
        super(context, PREFERENCES_NAME);
    }

    @Override
    public String getEventType() {
        return context.getString(R.string.event_chat);
    }

    @Override
    public int getPriority() {
        return 8;
    }

    @Override
    public String getSettingsAction() {
        return ACTION_SETTINGS;
    }
}
