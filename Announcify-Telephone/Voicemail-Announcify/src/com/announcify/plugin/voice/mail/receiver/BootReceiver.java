package com.announcify.plugin.voice.mail.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.announcify.plugin.voice.mail.service.VoicemailService;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(final Context context, final Intent intent) {
		context.startService(new Intent(context, VoicemailService.class));
	}
}
