package com.announcify.receiver;

import com.announcify.activity.control.RemoteControlDialog;
import com.announcify.util.HeadsetFinder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class HeadsetReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		if (HeadsetFinder.detectHeadset(context, intent)) {
			context.sendBroadcast(new Intent(RemoteControlDialog.ACTION_CONTINUE));
		} else {
			context.sendBroadcast(new Intent(RemoteControlDialog.ACTION_PAUSE));
		}
	}
}