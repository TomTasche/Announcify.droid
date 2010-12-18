package com.announcify.plugin.mail.util;

import android.content.Context;
import android.content.SharedPreferences;

public class Settings {
	public static final String PREFERENCES_NAME = "com.announcify.plugin.mail.SETTINGS";

	private final SharedPreferences preferences;

	private static final String KEY_READ_OWN = "preference_read_own";
	private static final String KEY_MAIL_ADDRESS = "preference_mail_address";

	public Settings(final Context context) {
		preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_WORLD_READABLE);
	}

	public boolean getReadOwn() {
		return preferences.getBoolean(KEY_READ_OWN, false);
	}

	public String getAddress() {
		return preferences.getString(KEY_MAIL_ADDRESS, "");
	}
}