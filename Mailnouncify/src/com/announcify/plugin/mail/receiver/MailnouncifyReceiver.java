package com.announcify.plugin.mail.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.announcify.plugin.mail.activity.Mailnouncify;

public class MailnouncifyReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(final Context context, final Intent intent) {
		final Intent respondIntent = new Intent("com.announcify.ACTION_PLUGIN_RESPOND");
		respondIntent.putExtra("com.announcify.EXTRA_PLUGIN_NAME", "Mailnouncify");
		respondIntent.putExtra("com.announcify.EXTRA_PLUGIN_ACTION", Mailnouncify.ACTION_SETTINGS);
		context.sendBroadcast(respondIntent);
	}
}