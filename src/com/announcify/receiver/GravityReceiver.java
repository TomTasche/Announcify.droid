package com.announcify.receiver;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;

import com.announcify.activity.control.RemoteControlDialog;
import com.announcify.receiver.gravity.GravityListener;

public class GravityReceiver extends GravityListener {
	public GravityReceiver(final Context context) {
		super(context, new GravityStateListener() {
			public void onDisplayDown() {
				context.sendBroadcast(new Intent(RemoteControlDialog.ACTION_PAUSE));

				final Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
				vibrator.vibrate(100);
			}

			public void onDisplayUp() {}
		});
	}
}