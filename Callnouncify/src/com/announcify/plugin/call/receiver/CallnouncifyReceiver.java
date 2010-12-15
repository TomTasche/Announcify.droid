package com.announcify.plugin.call.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.announcify.plugin.call.activity.Callnouncify;

public class CallnouncifyReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(final Context context, final Intent intent) {
		Log.e("smn", "call");

		final Intent respondIntent = new Intent("com.announcify.ACTION_PLUGIN_RESPOND");
		respondIntent.putExtra("com.announcify.EXTRA_PLUGIN_NAME", "Callnouncify");
		respondIntent.putExtra("com.announcify.EXTRA_PLUGIN_ACTION", Callnouncify.ACTION_SETTINGS);
		context.sendBroadcast(respondIntent);
	}
}