package com.announcify.error;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ExceptionReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Intent serviceIntent = new Intent(context, ExceptionService.class);
		serviceIntent.putExtras(intent.getExtras());
		context.startService(serviceIntent);
	}
}