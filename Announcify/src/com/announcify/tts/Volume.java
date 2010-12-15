package com.announcify.tts;

import android.content.Context;
import android.media.AudioManager;

import com.announcify.util.AnnouncifySettings;

public class Volume {
	private final AudioManager manager;

	private int previousVolume;
	private final int STREAM;

	public Volume(final Context context) {
		manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

		STREAM = new AnnouncifySettings(context).getStream();
	}

	private boolean isSilent() {
		return manager.getStreamVolume(STREAM) == 0 ? true : false;
	}

	public void lowerVolume() {
		if (isSilent()) {
			return;
		}
		previousVolume = manager.getStreamVolume(STREAM);
		manager.setStreamVolume(STREAM, 5, 0);
	}

	public void upperVolume() {
		if (previousVolume > 0) {
			manager.setStreamVolume(STREAM, previousVolume, 0);
		}
	}

	public void muteStreams() {
		if (isSilent()) {
			return;
		}
		manager.setStreamSolo(STREAM, true);
	}

	public void unmuteStreams() {
		manager.setStreamSolo(STREAM, false);
	}

	public void quit() {
		// TODO Auto-generated method stub
	}
}