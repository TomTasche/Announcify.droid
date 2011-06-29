package com.announcify.background.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import at.bartinger.toastplugin.SmartPhonePluginHelper;

import com.announcify.api.background.sql.model.PluginModel;

public class SmartPhoneReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        final PluginModel model = new PluginModel(context);
        String data = intent.getStringExtra(SmartPhonePluginHelper.SMART_PHONE_PLUGIN_DATA);
        
        model.setActive(model.getId("Announcify++"), new Boolean(data));
    }
}
