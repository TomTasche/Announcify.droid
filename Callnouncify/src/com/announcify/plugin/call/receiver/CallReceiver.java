
package com.announcify.plugin.call.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import com.announcify.plugin.call.service.WorkerService;

public class CallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (TelephonyManager.EXTRA_STATE_RINGING.equals(intent
                .getStringExtra(TelephonyManager.EXTRA_STATE))) {
            final Intent serviceIntent = new Intent(context, WorkerService.class);
            serviceIntent.putExtras(intent.getExtras());
            context.startService(serviceIntent);
        } else {
            final Intent ringtoneIntent = new Intent(RingtoneReceiver.ACTION_STOP_RINGTONE);
            context.sendBroadcast(ringtoneIntent);
        }
    }
}
