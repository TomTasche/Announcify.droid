
package com.announcify.plugin.message.mms.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.announcify.plugin.message.mms.service.WorkerService;

public class MessageReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (!intent.hasExtra("data")) {
            return;
        }
        final Intent serviceIntent = new Intent(context, WorkerService.class);
        serviceIntent.putExtras(intent.getExtras());
        context.startService(serviceIntent);
    }
}
