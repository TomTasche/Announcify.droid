package com.announcify.plugin.mail.util;

import android.content.Context;

import com.announcify.api.util.PluginSettings;

public class MailnouncifySettings extends PluginSettings {
	public static final String PREFERENCES_NAME = "com.announcify.plugin.mail.SETTINGS";

	private static final String KEY_READ_OWN = "preference_read_own";
	private static final String KEY_MAIL_ADDRESS = "preference_mail_address";

	public MailnouncifySettings(final Context context) {
		super(context, PREFERENCES_NAME);
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
}