package com.announcify.activity.control;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.announcify.R;
import com.announcify.util.Money;

public class RemoteControlDialogInstaller extends Activity {
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (Money.isPaid(this)) {
			final Intent shortcutIntent = new Intent(this, RemoteControlDialog.class);
			shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

			final Intent intent = new Intent();
			intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
			intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Announcify RemoteControl");
			intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(this, R.drawable.launcher_icon));

			setResult(RESULT_OK, intent);
		} else {
			Toast.makeText(this, "This feature is only available in Pro version", Toast.LENGTH_LONG).show();
			setResult(RESULT_CANCELED);
		}
		finish();
	}
}