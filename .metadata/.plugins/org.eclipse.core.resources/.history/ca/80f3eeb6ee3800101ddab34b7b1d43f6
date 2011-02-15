package com.announcify.plugin.message.mms.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.announcify.api.background.service.PluginService;
import com.announcify.plugin.message.mms.service.WorkerService;


public class MessageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        final Intent serviceIntent = new Intent(context, WorkerService.class);
        serviceIntent.setAction(PluginService.ACTION_ANNOUNCE);
        serviceIntent.putExtras(intent.getExtras());
        context.startService(serviceIntent);
    }
}
