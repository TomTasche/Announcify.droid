package com.announcify.plugin.talk.receiver;

import android.content.Context;
import android.content.Intent;

import com.announcify.api.receiver.PluginReceiver;
import com.announcify.plugin.talk.activity.Talknouncify;
import com.announcify.plugin.talk.service.TalkService;

public class TalknouncifyReceiver extends PluginReceiver {
    @Override
    public void onReceive(final Context context, final Intent intent) {
        final Intent respondIntent = new Intent(ACTION_PLUGIN_RESPOND);
        respondIntent.putExtra(EXTRA_PLUGIN_NAME, "Talknouncify");
        respondIntent.putExtra(EXTRA_PLUGIN_ACTION, Talknouncify.ACTION_SETTINGS);
        respondIntent.putExtra(EXTRA_PLUGIN_BROADCAST, false);
        respondIntent.putExtra(EXTRA_PLUGIN_PACKAGE, context.getPackageName());
        respondIntent.putExtra(EXTRA_PLUGIN_PRIORITY, 1);
        context.sendBroadcast(respondIntent);

        final Intent serviceIntent = new Intent(context, TalkService.class);
        context.startService(serviceIntent);
    }
}