package com.announcify.plugin.mail.receiver;

import android.content.Context;
import android.content.Intent;

import com.announcify.api.receiver.PluginReceiver;
import com.announcify.plugin.mail.activity.Mailnouncify;
import com.announcify.plugin.mail.service.MailService;

public class MailnouncifyReceiver extends PluginReceiver {
	@Override
	public void onReceive(final Context context, final Intent intent) {
	    final Intent respondIntent = new Intent(ACTION_PLUGIN_RESPOND);
        respondIntent.putExtra(EXTRA_PLUGIN_NAME, "Mailnouncify");
        respondIntent.putExtra(EXTRA_PLUGIN_ACTION, Mailnouncify.ACTION_SETTINGS);
        respondIntent.putExtra(EXTRA_PLUGIN_BROADCAST, false);
        respondIntent.putExtra(EXTRA_PLUGIN_PACKAGE, context.getPackageName());
        respondIntent.putExtra(EXTRA_PLUGIN_PRIORITY, 5);
        context.sendBroadcast(respondIntent);
		
		final Intent serviceIntent = new Intent(context, MailService.class);
		context.startService(serviceIntent);
	}
}