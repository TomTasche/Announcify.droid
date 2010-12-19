package com.announcify.plugin.talk.util;

import android.content.Context;

import com.announcify.util.PluginSettings;

public class TalknouncifySettings extends PluginSettings {
	public static final String PREFERENCES_NAME = "com.announcify.plugin.talk.SETTINGS";
	
	public TalknouncifySettings(Context context) {
		super(context, PREFERENCES_NAME);
	}

	@Override
	public String getEventType() {
		return "Chat";
	}
}
