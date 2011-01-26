
package com.announcify.plugin.message.sms.util;

import android.content.Context;

import com.announcify.api.util.PluginSettings;

public class Settings extends PluginSettings {
    public static final String PREFERENCES_NAME = "com.announcify.plugin.message.sms.SETTINGS";

    public Settings(final Context context) {
        super(context, PREFERENCES_NAME);
    }

    @Override
    public String getEventType() {
        return "SMS";
    }

    @Override
    public int getPriority() {
        return 2;
    }
}
