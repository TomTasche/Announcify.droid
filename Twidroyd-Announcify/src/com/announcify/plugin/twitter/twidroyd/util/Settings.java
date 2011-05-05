package com.announcify.plugin.twitter.twidroyd.util;

import android.content.Context;

import com.announcify.api.background.util.PluginSettings;
import com.announcify.plugin.twitter.twidroyd.R;

public class Settings extends PluginSettings {

    public static final String ACTION_SETTINGS = "com.announcify.plugin.twitter.twidroyd.SETTINGS";
    public static final String PREFERENCES_NAME = "com.announcify.plugin.twitter.twidroyd";

    public Settings(final Context context) {
        super(context, PREFERENCES_NAME);
    }

    @Override
    public String getEventType() {
        return context.getString(R.string.event_twidroyd);
    }

    @Override
    public int getPriority() {
        return 7;
    }

    @Override
    public String getSettingsAction() {
        return ACTION_SETTINGS;
    }
}
