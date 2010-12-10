package com.announcify.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.telephony.TelephonyManager;

import com.announcify.activity.control.RemoteControlDialog;

public class ScreenReceiver extends BroadcastReceiver {
	private boolean screenOn;

	public ScreenReceiver(final Context context) {
		screenOn = ((PowerManager) context.getSystemService(Context.POWER_SERVICE)).isScreenOn();
	}

	@Override
	public void onReceive(final Context context, final Intent intent) {
		if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
			if (((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getCallState() == TelephonyManager.CALL_STATE_RINGING) {
				return;
			}

			screenOn = true;
			// TODO: do only one of them
			// manager.lowerSpeechVolume();
			context.sendBroadcast(new Intent(RemoteControlDialog.ACTION_PAUSE));
		} else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
			screenOn = false;
			// manager.upperSpeechVolume();
		}
	}

	public boolean isScreenOn() {
		return screenOn;
	}
}