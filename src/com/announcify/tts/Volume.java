package com.announcify.tts;

import android.content.Context;
import android.media.AudioManager;

public class Volume {
	private final Context context;
	private final AudioManager manager;

	private int previousVolume;

	// private final OnAudioFocusChangeListener focusListener;

	// TODO: make customizable
	public Volume(final Context context) {
		this.context = context;
		manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		// focusListener = new OnAudioFocusChangeListener() {
		//
		// public void onAudioFocusChange(int focusChange) {
		// // TODO: lower volume? upper volume?
		// }
		// };
	}

	// public void requestFocus() {
	// manager.requestAudioFocus(focusListener, AudioManager.STREAM_RING,
	// AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);
	// }
	//
	// public void loseFocus() {
	// manager.abandonAudioFocus(focusListener);
	// }

	private boolean isSilent() {
		return manager.getStreamVolume(AudioManager.STREAM_RING) == 0 ? true : false;
	}

	public void lowerMusicVolume() {
		if (isSilent()) {
			return;
		}
		manager.setStreamVolume(AudioManager.STREAM_MUSIC, 5, 0);
	}

	public void lowerSpeechVolume() {
		if (isSilent()) {
			return;
		}
		previousVolume = manager.getStreamVolume(AudioManager.STREAM_RING);
		manager.setStreamVolume(AudioManager.STREAM_RING, 5, 0);
	}

	public void upperMusicVolume() {
		manager.setStreamVolume(AudioManager.STREAM_MUSIC, 15, 0);
	}

	public void upperSpeechVolume() {
		if (previousVolume > 0) {
			manager.setStreamVolume(AudioManager.STREAM_RING, previousVolume, 0);
		}
	}

	public void muteStreams() {
		if (isSilent()) {
			return;
		}
		manager.setStreamSolo(AudioManager.STREAM_MUSIC, true);
	}

	public void unmuteStreams() {
		manager.setStreamSolo(AudioManager.STREAM_MUSIC, false);
	}

	public void quit() {
		// TODO Auto-generated method stub
	}
}