package com.announcify.background.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import at.bartinger.toastplugin.SmartPhonePluginHelper;

import com.announcify.api.background.sql.model.PluginModel;

public class SmartPhoneReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
    	if(intent.hasExtra(SmartPhonePluginHelper.SMART_PHONE_PLUGIN_DATA)){
			final String data = intent.getStringExtra(SmartPhonePluginHelper.SMART_PHONE_PLUGIN_DATA);

			final boolean toggleBack = intent.getBooleanExtra(SmartPhonePluginHelper.SMART_PHONE_PLUGIN_TOGGLE_BACK, false);

			final PluginModel model = new PluginModel(context);
			
			boolean toggle = new Boolean(data);
			if (toggleBack) toggle = !toggle;
			
	        model.setActive(model.getId("Announcify++"), toggle);
		}
    }
}
