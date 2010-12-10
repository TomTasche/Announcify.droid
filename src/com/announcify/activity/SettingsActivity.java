package com.announcify.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.announcify.R;
import com.announcify.util.Money;
import com.announcify.util.Settings;

public class SettingsActivity extends PreferenceActivity {
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences_settings);
		getPreferenceManager().setSharedPreferencesName(Settings.PREFERENCES_NAME);
		getPreferenceManager().setSharedPreferencesMode(Context.MODE_WORLD_READABLE);
	}

	@Override
	public boolean dispatchKeyEvent(final KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			finish();
			return false;
		}

		return nagUser();
	}

	@Override
	public boolean dispatchTouchEvent(final MotionEvent ev) {
		return nagUser();
	}

	@Override
	public boolean dispatchTrackballEvent(final MotionEvent ev) {
		return nagUser();
	}

	private boolean nagUser() {
		if (Money.isPaid(this)) {
			return false;
		} else {
			Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Please upgrade to Pro Version");
			builder.setMessage("These settings are only available for Pro users, sorry! Please buy the Pro Version from Android Market.");
			builder.create().show();
			
			return true;
		}
	}
}