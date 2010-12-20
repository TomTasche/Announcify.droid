package com.announcify.plugin.call.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import com.announcify.util.AnnouncifySettings;

public class RingtoneReceiver extends BroadcastReceiver {
	public static final String ACTION_START_RINGTONE = "com.announcify.plugin.call.ACTION_START_RINGTONE";
	public static final String ACTION_STOP_RINGTONE = "com.announcify.plugin.call.ACTION_STOP_RINGTONE";

	private static Ringtone ringtone;

	@Override
	public void onReceive(final Context context, final Intent intent) {
		if (ACTION_START_RINGTONE.equals(intent.getAction())) {
			final String s = context.getSharedPreferences("com.announcify.plugin.call.SETTINGS", Context.MODE_WORLD_READABLE).getString("preference_ringtone", "");
			if (s == null || "".equals(s)) {
				return;
			}
			final RingtoneManager manager = new RingtoneManager(context);
			manager.setType(RingtoneManager.TYPE_RINGTONE);
			ringtone = manager.getRingtone(manager.getRingtonePosition(Uri.parse(s)));
			if (ringtone == null) {
				return;
			}
			ringtone.setStreamType(new AnnouncifySettings(context).getStream());
			ringtone.play();
		} else if (ACTION_STOP_RINGTONE.equals(intent.getAction())) {
			if (ringtone != null) {
				ringtone.stop();
				ringtone = null;
			}
		}
	}
}