
package com.announcify.plugin.call.receiver;

import android.content.Context;
import android.content.Intent;

import com.announcify.api.receiver.PluginReceiver;
import com.announcify.plugin.call.activity.Callnouncify;

public class CallnouncifyReceiver extends PluginReceiver {
    @Override
    public void onReceive(final Context context, final Intent intent) {
        final Intent respondIntent = new Intent(ACTION_PLUGIN_RESPOND);
        respondIntent.putExtra(EXTRA_PLUGIN_NAME, "Callnouncify");
        respondIntent.putExtra(EXTRA_PLUGIN_ACTION, Callnouncify.ACTION_SETTINGS);
        respondIntent.putExtra(EXTRA_PLUGIN_BROADCAST, false);
        respondIntent.putExtra(EXTRA_PLUGIN_PACKAGE, context.getPackageName());
        respondIntent.putExtra(EXTRA_PLUGIN_PRIORITY, 1);
        context.sendBroadcast(respondIntent);
    }
}
