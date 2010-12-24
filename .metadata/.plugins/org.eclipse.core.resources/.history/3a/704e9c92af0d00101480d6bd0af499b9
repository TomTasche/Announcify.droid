package com.announcify.service;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.announcify.receiver.CallReceiver;
import com.announcify.receiver.GravityReceiver;
import com.announcify.receiver.HeadsetReceiver;
import com.announcify.receiver.ScreenReceiver;
import com.announcify.receiver.gravity.GravityListener;
import com.announcify.util.AnnouncifySettings;

public class ConditionManager {
	private final Context context;

	private ScreenReceiver screenReceiver;
	private GravityListener gravityReceiver;
	private final CallReceiver callReceiver;
	private HeadsetReceiver headsetReceiver;

	public ConditionManager(final Context context, final AnnouncifySettings settings) {
		this.context = context;

		if (settings.isGravityCondition()) {
			gravityReceiver = new GravityReceiver(context);
			gravityReceiver.setAccuracy(2f);
		}

		if (settings.isScreenCondition()) {
			screenReceiver = new ScreenReceiver(context);
			final IntentFilter screenFilter = new IntentFilter();
			screenFilter.addAction(Intent.ACTION_SCREEN_ON);
			screenFilter.addAction(Intent.ACTION_SCREEN_OFF);
			context.registerReceiver(screenReceiver, screenFilter);
		}

		if (settings.isDiscreetCondition()) {
			headsetReceiver = new HeadsetReceiver();
			context.registerReceiver(headsetReceiver, new IntentFilter(Intent.ACTION_HEADSET_PLUG));
		}

		callReceiver = new CallReceiver(context);
		((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).listen(callReceiver, PhoneStateListener.LISTEN_CALL_STATE);
	}

	public boolean isScreenOn() {
		return screenReceiver != null ? screenReceiver.isScreenOn() : false;
	}

	public void setOnCall(final boolean onCall) {
		callReceiver.setOnCall(onCall);
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
		if (headsetReceiver != null) {
			context.unregisterReceiver(headsetReceiver);
		}
	}
}
