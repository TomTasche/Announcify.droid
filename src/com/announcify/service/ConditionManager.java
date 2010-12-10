package com.announcify.service;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.announcify.receiver.CallReceiver;
import com.announcify.receiver.GravityReceiver;
import com.announcify.receiver.ScreenReceiver;
import com.announcify.receiver.gravity.GravityListener;

public class ConditionManager {
	private final Context context;

	private final ScreenReceiver screenReceiver;
	private final GravityListener gravityReceiver;
	private final CallReceiver callReceiver;

	public ConditionManager(final Context context) {
		this.context = context;

		gravityReceiver = new GravityReceiver(context);
		gravityReceiver.setAccuracy(2f);

		screenReceiver = new ScreenReceiver(context);
		final IntentFilter screenFilter = new IntentFilter();
		screenFilter.addAction(Intent.ACTION_SCREEN_ON);
		screenFilter.addAction(Intent.ACTION_SCREEN_OFF);
		context.registerReceiver(screenReceiver, screenFilter);

		callReceiver = new CallReceiver(context);
		((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).listen(callReceiver, PhoneStateListener.LISTEN_CALL_STATE);
	}

	public boolean isScreenOn() {
		return screenReceiver.isScreenOn();
	}

	public void quit() {
		if (screenReceiver != null) {
			context.unregisterReceiver(screenReceiver);
		}
		if (callReceiver != null) {
			((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).listen(callReceiver, android.telephony.PhoneStateListener.LISTEN_NONE);
		}
		if (gravityReceiver != null) {
			gravityReceiver.stopAccelerometer();
		}
	}
}
