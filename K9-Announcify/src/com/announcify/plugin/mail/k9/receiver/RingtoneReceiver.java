package com.announcify.plugin.mail.k9.receiver;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.announcify.plugin.mail.k9.service.WorkerService;


public class RingtoneReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        intent.setComponent(new ComponentName(context, WorkerService.class));
        context.startService(intent);
    }
}
