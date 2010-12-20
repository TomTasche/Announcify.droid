package com.announcify.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.announcify.activity.control.RemoteControlDialog;
import com.announcify.util.HeadsetFinder;

public class HeadsetReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(final Context context, final Intent intent) {
		if (HeadsetFinder.detectHeadset(context, intent)) {
			context.sendBroadcast(new Intent(RemoteControlDialog.ACTION_CONTINUE));
		} else {
			context.sendBroadcast(new Intent(RemoteControlDialog.ACTION_PAUSE));
		}
	}
}