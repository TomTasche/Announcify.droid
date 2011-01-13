
package com.announcify.plugin.call.util;

import android.content.Context;

import com.announcify.api.util.PluginSettings;

public class CallnouncifySettings extends PluginSettings {
    public static final String PREFERENCES_NAME = "com.announcify.plugin.call.SETTINGS";

    public CallnouncifySettings(final Context context) {
        super(context, PREFERENCES_NAME);
    }

    @Override
    public int getReadingRepeat() {
        return Integer.parseInt(preferences.getString(KEY_READING_REPEAT, "5"));
    }

    @Override
    public String getEventType() {
        // TODO: localize
        return "Call";
    }
}
