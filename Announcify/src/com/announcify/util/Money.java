package com.announcify.util;

import android.content.Context;

public class Money {
	public static boolean isPaid(final Context context) {
		return !context.getSharedPreferences(AnnouncifySettings.PREFERENCES_NAME, Context.MODE_WORLD_READABLE).getBoolean("test", true);
	}
}