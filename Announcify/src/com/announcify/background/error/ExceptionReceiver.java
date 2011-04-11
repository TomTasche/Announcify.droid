package com.announcify.background.error;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ExceptionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        final Intent serviceIntent = new Intent(context, ExceptionService.class);
        serviceIntent.putExtras(intent.getExtras());
        context.startService(serviceIntent);
    }
}
