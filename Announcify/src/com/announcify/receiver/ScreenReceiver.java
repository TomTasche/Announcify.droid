
package com.announcify.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.announcify.activity.control.RemoteControlDialog;

public class ScreenReceiver extends BroadcastReceiver {
    private boolean screenOn;

    public ScreenReceiver(final Context context) {
        screenOn = ((PowerManager)context.getSystemService(Context.POWER_SERVICE)).isScreenOn();
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            if (((TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE))
                    .getCallState() == TelephonyManager.CALL_STATE_RINGING) {
                return;
            }

            screenOn = true;
            context.sendBroadcast(new Intent(RemoteControlDialog.ACTION_PAUSE));
            Log.e("Announcify", "Shutdown because: Screen");

            Toast.makeText(
                    context,
                    "I've decided to stay silent and shut up, because I don't want to disturb you. Please check notifications for new notifications.",
                    Toast.LENGTH_LONG).show();
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            screenOn = false;
            // manager.upperSpeechVolume();
        }
    }

    public boolean isScreenOn() {
        return screenOn;
    }
}
