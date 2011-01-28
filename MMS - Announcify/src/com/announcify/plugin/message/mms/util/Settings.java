
package com.announcify.plugin.message.mms.util;

import android.content.Context;

import com.announcify.api.background.util.PluginSettings;

public class Settings extends PluginSettings {

    public static final String ACTION_SETTINGS = "com.announcify.plugin.message.mms.SETTINGS";

    public Settings(final Context context) {
        super(context);
    }

    @Override
    public String getEventType() {
        return "MMS";
    }

    @Override
    public int getPriority() {
        return 2;
    }

    @Override
    public String getSettingsAction() {
        return ACTION_SETTINGS;
    }
}
