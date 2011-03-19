package com.announcify.background.error;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ExceptionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.e("smn", "received");

        final Intent serviceIntent = new Intent(context, ExceptionService.class);
        serviceIntent.putExtras(intent.getExtras());
        context.startService(serviceIntent);
    }
}
