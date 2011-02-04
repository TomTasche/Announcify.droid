package com.announcify.background.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.widget.Toast;

import com.announcify.R;
import com.announcify.api.background.error.ExceptionHandler;
import com.announcify.api.background.service.PluginService;
import com.announcify.api.background.util.AnnouncifySettings;
import com.announcify.background.handler.AnnouncificationHandler;
import com.announcify.background.receiver.ControlReceiver;
import com.announcify.background.tts.Speaker;
import com.announcify.ui.control.RemoteControlDialog;


public class ManagerService extends Service {

    private NotificationManager notificationManager;

    private ConditionManager conditionManager;

    private ControlReceiver controlReceiver;

    private AnnouncificationHandler handler;

    private HandlerThread thread;

    private Speaker speaker;

    @Override
    public IBinder onBind(final Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        final AnnouncifySettings settings = new AnnouncifySettings(this);

        if (settings.isShowNotification()) {
            final PendingIntent pendingIntent = PendingIntent.getActivity(this,
                    1993, new Intent(this, RemoteControlDialog.class), 0);
            final Notification notification = new Notification(
                    R.drawable.notification_icon, null, 0);
            notification.setLatestEventInfo(this, "Important Announcification",
                    "Press here to stop it.", pendingIntent);
            startForeground(17, notification);
        }

        conditionManager = new ConditionManager(this, settings);
        // if (conditionManager.isScreenOn()) {
        // manager.lowerSpeechVolume();
        // }

        thread = new HandlerThread("Announcifications");
        thread.start();

        speaker = new Speaker(ManagerService.this, new OnInitListener() {

            public void onInit(final int status) {
                handler.sendEmptyMessage(AnnouncificationHandler.WHAT_START);
            }
        });

        handler = new AnnouncificationHandler(ManagerService.this,
                thread.getLooper(), speaker);
        handler.post(new Runnable() {

            public void run() {
                Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(
                        ManagerService.this, Thread
                                .getDefaultUncaughtExceptionHandler()));
            }
        });

        controlReceiver = new ControlReceiver(handler);
        final IntentFilter controlFilter = new IntentFilter();
        controlFilter.addAction(RemoteControlDialog.ACTION_CONTINUE);
        controlFilter.addAction(RemoteControlDialog.ACTION_PAUSE);
        controlFilter.addAction(RemoteControlDialog.ACTION_SKIP);
        registerReceiver(controlReceiver, controlFilter);
    }

    @Override
    public void onDestroy() {
        Log.e("Announcify", "shutdown");

        if (handler != null) {
            final Message msg = handler
                    .obtainMessage(AnnouncificationHandler.WHAT_SHUTDOWN);
            handler.sendMessage(msg);
        }

        if (controlReceiver != null) {
            unregisterReceiver(controlReceiver);
        }

        conditionManager.quit();

        if (speaker != null) {
            speaker.shutdown();
        }

        if ((thread != null) && thread.isAlive()) {
            thread.interrupt();
            thread.getLooper().quit();
        }

        if (notificationManager != null) {
            notificationManager.cancel(17);
        }

        super.onDestroy();
    }

    @Override
    public void onStart(final Intent intent, final int startId) {
        super.onStart(intent, startId);

        if ((intent == null) || (intent.getExtras() == null)) return;

        if ((intent.getExtras().getInt(PluginService.EXTRA_PRIORITY, -1) > 1)
                && conditionManager.isScreenOn()) {
            Toast.makeText(
                    this,
                    "I've decided to stay silent and shut up, because I don't want to disturb you. Please check notifications for new notifications.",
                    Toast.LENGTH_LONG).show();

            return;
        }

        if (intent.getExtras().getInt(PluginService.EXTRA_PRIORITY, -1) == 1) {
            conditionManager.setOnCall(true);
        }

        final Message msg = handler
                .obtainMessage(AnnouncificationHandler.WHAT_PUT_QUEUE);
        msg.setData(intent.getExtras());
        handler.sendMessage(msg);
    }
}
