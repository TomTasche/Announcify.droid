package com.announcify.background.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.announcify.ui.control.RemoteControlDialog;

public class ScreenReceiver extends BroadcastReceiver {

    private final Context context;

    public ScreenReceiver(final Context context) {
        this.context = context;
    }

    public boolean isScreenOn() {
        return ((PowerManager) context.getSystemService(Context.POWER_SERVICE)).isScreenOn();
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            if (((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getCallState() == TelephonyManager.CALL_STATE_RINGING) {
                return;
            }

            context.sendBroadcast(new Intent(RemoteControlDialog.ACTION_PAUSE));
            Log.e("Announcify", "Shutdown because: Screen");
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            // manager.upperSpeechVolume();
        }
    }
}
