package com.announcify.plugin.voice.mail.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.announcify.api.background.error.ExceptionHandler;


public class VoicemailService extends Service {

    private class VoicemailListener extends PhoneStateListener {

        @Override
        public void onMessageWaitingIndicatorChanged(final boolean mwi) {
            startService(new Intent(VoicemailService.this, WorkerService.class));
        }
    }

    private PhoneStateListener listener;

    @Override
    public IBinder onBind(final Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        listener = new VoicemailListener();
        ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).listen(
                listener, PhoneStateListener.LISTEN_MESSAGE_WAITING_INDICATOR);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).listen(
                listener, PhoneStateListener.LISTEN_NONE);
    }
}
