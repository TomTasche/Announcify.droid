package com.announcify.background.error;

import org.acra.ErrorReporter;

import android.app.IntentService;
import android.content.Intent;

import com.announcify.api.background.error.ExceptionHandler;


public class ExceptionService extends IntentService {

    public ExceptionService() {
        super("ExceptionService");
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        final ErrorReporter reporter = ErrorReporter.getInstance();
        reporter.putCustomData("plugin",
                intent.getStringExtra(ExceptionHandler.PACKAGE));
        reporter.handleException((Throwable) intent
                .getSerializableExtra(ExceptionHandler.STACKTRACE));
        reporter.removeCustomData("plugin");
    }
}
