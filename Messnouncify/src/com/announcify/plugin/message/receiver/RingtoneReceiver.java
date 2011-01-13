
package com.announcify.plugin.message.receiver;

import java.io.IOException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;

import com.announcify.api.util.AnnouncifySettings;

public class RingtoneReceiver extends BroadcastReceiver {
    public static final String ACTION_START_RINGTONE = "com.announcify.plugin.message.ACTION_START_RINGTONE";

    public static final String ACTION_STOP_RINGTONE = "com.announcify.plugin.message.ACTION_STOP_RINGTONE";

    @SuppressWarnings("unused")
    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (ACTION_STOP_RINGTONE.equals(intent.getAction())) {
            final String s = context.getSharedPreferences("com.announcify.plugin.message.SETTINGS",
                    Context.MODE_WORLD_READABLE).getString("preference_ringtone", "");
            if (s == null || "".equals(s)) {
                return;
            }

            final MediaPlayer player = new MediaPlayer();
            if (player == null) {
                return;
            }

            try {
                player.setDataSource(context, Uri.parse(s));
                player.setAudioStreamType(new AnnouncifySettings(context).getStream());
                player.setLooping(false);
                player.setOnCompletionListener(new OnCompletionListener() {

                    public void onCompletion(final MediaPlayer mp) {
                        mp.release();
                    }
                });
                player.prepare();
                player.start();
            } catch (final IllegalArgumentException e) {
                e.printStackTrace();
            } catch (final SecurityException e) {
                e.printStackTrace();
            } catch (final IllegalStateException e) {
                e.printStackTrace();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }
}
