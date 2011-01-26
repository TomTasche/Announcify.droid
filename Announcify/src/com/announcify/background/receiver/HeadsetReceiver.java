
package com.announcify.background.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.announcify.api.background.audio.HeadsetFinder;
import com.announcify.ui.control.RemoteControlDialog;

public class HeadsetReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (HeadsetFinder.detectHeadset(context, intent)) {
            context.sendBroadcast(new Intent(RemoteControlDialog.ACTION_CONTINUE));
        } else {
            context.sendBroadcast(new Intent(RemoteControlDialog.ACTION_PAUSE));
            Log.e("Announcify", "Shutdown because: Headset");
        }
    }
}
