package com.announcify.background.receiver;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;

import com.announcify.background.receiver.gravity.GravityListener;
import com.announcify.ui.control.RemoteControlDialog;

public class GravityReceiver extends GravityListener {

    public GravityReceiver(final Context context) {
        super(context, new GravityStateListener() {

            public void onDisplayDown() {
                context.sendBroadcast(new Intent(
                        RemoteControlDialog.ACTION_PAUSE));
                Log.e("Announcify", "Shutdown because: Gravity");

                final Vibrator vibrator = (Vibrator) context
                        .getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(100);
            }

            public void onDisplayUp() {
            }
        });
    }
}
