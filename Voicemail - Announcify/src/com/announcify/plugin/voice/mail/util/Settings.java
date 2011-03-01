package com.announcify.plugin.voice.mail.util;

import android.content.Context;

import com.announcify.api.background.util.PluginSettings;
import com.announcify.plugin.voice.mail.R;


public class Settings extends PluginSettings {

    public static final String ACTION_SETTINGS = "com.announcify.plugin.voice.mail.SETTINGS";

    public static final String PREFERENCES_NAME = "com.announcify.plugin.voice.mail";

    public Settings(final Context context) {
        super(context, PREFERENCES_NAME);
    }

    @Override
    public String getEventType() {
        return context.getString(R.string.event_voicemail);
    }

    @Override
    public int getPriority() {
        return 4;
    }

    @Override
    public String getSettingsAction() {
        return ACTION_SETTINGS;
    }
}
