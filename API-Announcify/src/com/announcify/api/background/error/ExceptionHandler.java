package com.announcify.api.background.error;

import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.announcify.api.background.util.AnnouncifySettings;

public class ExceptionHandler implements UncaughtExceptionHandler {

	private final Context context;
	private final AnnouncifySettings settings;
	private final UncaughtExceptionHandler defaultHandler;

	public ExceptionHandler(final Context context) {
		this.context = context;
		this.defaultHandler = Thread.getDefaultUncaughtExceptionHandler();

		settings = new AnnouncifySettings(context);
	}

	public void uncaughtException(final Thread thread, final Throwable exception) {
		Log.e("Announcify", "Reporting exception", exception);

		if (settings.isAutomaticErrorReporting()) {
			Intent intent = ReportUtil.createFeedbackIntent(context, exception);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			context.startActivity(intent);
		}

		defaultHandler.uncaughtException(thread, exception);
	}
}
