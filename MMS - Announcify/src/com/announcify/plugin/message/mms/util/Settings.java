
package com.announcify.plugin.message.mms.util;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import com.announcify.api.background.util.PluginSettings;
import com.announcify.plugin.message.mms.R;

public class Settings extends PluginSettings {

    public static final String ACTION_SETTINGS = "com.announcify.plugin.message.mms.SETTINGS";
    
    public static final String PREFERENCES_NAME = "com.announcify.plugin.message.mms";

    public Settings(final Context context) {
        super(context, PREFERENCES_NAME);
    }

    @Override
    public String getEventType() {
        return context.getString(R.string.event_mms);
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
