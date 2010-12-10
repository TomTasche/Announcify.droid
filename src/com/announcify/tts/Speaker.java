package com.announcify.tts;

import java.util.HashMap;
import java.util.Locale;

import android.content.Context;
import android.media.AudioManager;
import android.speech.tts.TextToSpeech;

public class Speaker extends TextToSpeech {
	private final HashMap<String, String> params;

	public Speaker(final Context context, final OnInitListener listener) {
		super(context, listener);

		params = new HashMap<String, String>();
		params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "com.announcify.UTTERANCE_ID");
		params.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(AudioManager.STREAM_RING));
	}

	@Override
	public int speak(final String text, final int queueMode, final HashMap<String, String> params) {
		if (super.speak(text, queueMode, params) == TextToSpeech.SUCCESS) {
			return TextToSpeech.SUCCESS;
		} else {
			// TODO: send log to server
			return TextToSpeech.ERROR;
		}
	}

	public void speak(String text) {
		if (text == null) {
			text = "";
		}
		speak(text, TextToSpeech.QUEUE_ADD, params);
	}

	public void setLanguage(final Speech speech, final String textSnippet) {
		super.setLanguage(speech.getLanguage(textSnippet));
	}

	public void revertLanguage() {
		setLanguage(Locale.getDefault());
	}

	synchronized public void interrupt() {
		super.stop();
	}

	@Override
	synchronized public void shutdown() {
		interrupt();
		super.shutdown();
	}
}