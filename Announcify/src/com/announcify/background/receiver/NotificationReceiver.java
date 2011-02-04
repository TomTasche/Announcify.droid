package com.announcify.background.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.announcify.background.service.ManagerService;


public class NotificationReceiver extends BroadcastReceiver {

    public static final String NOTIFICATION_BROADCAST = "com.announcify.NOTIFICATION_CLICKED";

    @Override
    public void onReceive(final Context context, final Intent intent) {
        context.stopService(new Intent(context, ManagerService.class));
    }
}
