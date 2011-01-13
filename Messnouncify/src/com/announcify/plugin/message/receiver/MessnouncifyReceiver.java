
package com.announcify.plugin.message.receiver;

import android.content.Context;
import android.content.Intent;

import com.announcify.api.receiver.PluginReceiver;
import com.announcify.plugin.message.activity.Messnouncify;

public class MessnouncifyReceiver extends PluginReceiver {
    @Override
    public void onReceive(final Context context, final Intent intent) {
        final Intent respondIntent = new Intent(ACTION_PLUGIN_RESPOND);
        respondIntent.putExtra(EXTRA_PLUGIN_NAME, "Messnouncify");
        respondIntent.putExtra(EXTRA_PLUGIN_ACTION, Messnouncify.ACTION_SETTINGS);
        respondIntent.putExtra(EXTRA_PLUGIN_BROADCAST, false);
        respondIntent.putExtra(EXTRA_PLUGIN_PACKAGE, context.getPackageName());
        respondIntent.putExtra(EXTRA_PLUGIN_PRIORITY, 5);
        context.sendBroadcast(respondIntent);
    }
}
