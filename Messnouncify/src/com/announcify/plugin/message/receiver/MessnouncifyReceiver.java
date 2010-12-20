package com.announcify.plugin.message.receiver;

import android.content.Context;
import android.content.Intent;

import com.announcify.plugin.message.activity.Messnouncify;
import com.announcify.receiver.AnnouncifyReceiver;

public class MessnouncifyReceiver extends AnnouncifyReceiver {
	@Override
	public void onReceive(final Context context, final Intent intent) {
		final Intent respondIntent = new Intent(ACTION_PLUGIN_RESPOND);
		respondIntent.putExtra(EXTRA_PLUGIN_NAME, "Messnouncify");
		respondIntent.putExtra(EXTRA_PLUGIN_ACTION, Messnouncify.ACTION_SETTINGS);
		context.sendBroadcast(respondIntent);
	}
}