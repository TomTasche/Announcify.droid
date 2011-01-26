
package com.announcify.plugin.mail.google.util;

import android.content.Context;

import com.announcify.api.util.PluginSettings;

public class Settings extends PluginSettings {

    public static final String PREFERENCES_NAME = "com.announcify.plugin.mail.google.SETTINGS";

    private static final String KEY_READ_OWN = "preference_read_own";

    private static final String KEY_MAIL_ADDRESS = "preference_mail_address";

    private static final String KEY_MESSAGE = "preference_read_message";


    public Settings(final Context context) {
        super(context, PREFERENCES_NAME);
    }

    
    public int getReadMessageMode() {
        return preferences.getInt(KEY_MESSAGE, 0);
    }

    public boolean getReadOwn() {
        return preferences.getBoolean(KEY_READ_OWN, false);
    }

    public String getAddress() {
        return preferences.getString(KEY_MAIL_ADDRESS, "");
    }

    @Override
    public String getEventType() {
        return "Mail";
    }

    @Override
    public int getPriority() {
        return 5;
    }
}
