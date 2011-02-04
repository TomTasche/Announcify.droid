package com.announcify;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;

import android.app.Application;


@ReportsCrashes(formKey = "dGlaZGFQcDJNOFgtbUJSRnVnSWpmN3c6MQ")
public class AnnouncifyApplication extends Application {

    @Override
    public void onCreate() {
        ACRA.init(this);

        super.onCreate();
    }
}
