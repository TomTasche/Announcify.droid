
package com.announcify.plugin.voice.mail.util;

import android.content.Context;

import com.announcify.api.util.PluginSettings;

public class Settings extends PluginSettings {
    public static final String PREFERENCES_NAME = "com.announcify.plugin.voice.mail.SETTINGS";

    public Settings(final Context context) {
        super(context, PREFERENCES_NAME);
    }

    @Override
    public String getEventType() {
        // TODO: localize
        return "Voicemail";
    }

    @Override
    public int getPriority() {
        return 3;
    }
}
