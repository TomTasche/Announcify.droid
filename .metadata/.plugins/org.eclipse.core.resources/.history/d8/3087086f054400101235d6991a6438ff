package com.announcify.background.receiver;

import com.announcify.api.background.sql.model.PluginModel;
import com.announcify.background.queue.WakeLocker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class LocaleReceiver extends BroadcastReceiver {
    
    public static final String ANNOUNCIFY_ENABLED = "com.announcify.locale.ANNOUNCIFY_ENABLED";

    @Override
    public void onReceive(Context context, Intent intent) {
        PluginModel model = new PluginModel(context);
        model.setActive(model.getId("Announcify++"), intent.getBooleanExtra(ANNOUNCIFY_ENABLED, true));
    }
}
