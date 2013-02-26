package com.announcify.background.receiver;

import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.announcify.ui.control.RemoteControlDialog;

public class CallReceiver extends PhoneStateListener {

	private final Context context;

	private boolean onCall;

	public CallReceiver(final Context context) {
		this.context = context;
	}

	@Override
	public void onCallStateChanged(final int state, final String incomingNumber) {
		if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
			onCall = true;

			context.sendBroadcast(new Intent(RemoteControlDialog.ACTION_PAUSE));
		} else if (state == TelephonyManager.CALL_STATE_IDLE) {
			onCall = false;
		}
	}

	public boolean isOnCall() {
		return onCall;
	}
}
