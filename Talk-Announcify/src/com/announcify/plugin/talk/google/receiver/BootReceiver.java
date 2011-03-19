package com.announcify.plugin.talk.google.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.announcify.plugin.talk.google.service.TalkService;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        context.startService(new Intent(context, TalkService.class));
    }
}
