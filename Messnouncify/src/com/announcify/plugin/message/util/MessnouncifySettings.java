
package com.announcify.plugin.message.util;

import android.content.Context;

import com.announcify.api.util.PluginSettings;

public class MessnouncifySettings extends PluginSettings {
    public static final String PREFERENCES_NAME = "com.announcify.plugin.message.SETTINGS";

    public MessnouncifySettings(final Context context) {
        super(context, PREFERENCES_NAME);
    }

    @Override
    public String getEventType() {
        return "Message";
    }
}
