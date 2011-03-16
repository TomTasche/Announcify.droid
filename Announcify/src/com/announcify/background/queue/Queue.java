package com.announcify.background.queue;

import java.util.LinkedList;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;
import android.util.Log;

import com.announcify.api.background.queue.PluginQueue;
import com.announcify.background.handler.AnnouncificationHandler;


public class Queue implements OnUtteranceCompletedListener {

    public static final String EXTRA_TEXT_SNIPPET = "com.announcify.EXTRA_TEXT_SNIPPET";

    private final Context context;
    private final AnnouncificationHandler handler;
    private final LinkedList<PluginQueue> queue;

    private boolean started;
    private boolean granted;

    public Queue(final Context context, final AnnouncificationHandler handler) {
        queue = new LinkedList<PluginQueue>();
        this.context = context;
        this.handler = handler;
    }

    private void checkNext() {
        if (!queue.isEmpty() && queue.getFirst().isEmpty()) {
            context.sendBroadcast(new Intent(queue.getFirst().getStopBroadcast()));
            queue.removeFirst();

            if (queue.isEmpty()) {
                quit();
                return;
            }

            context.sendBroadcast(new Intent(queue.getFirst().getStartBroadcast()));
        }

        if (queue.isEmpty()) {
            quit();
            return;
        }
    }

    public void deny() {
        granted = false;

        WakeLocker.unlock();
    }

    public void grant() {
        if (!started) return;

        granted = true;
        WakeLocker.lock(context);

        next();
    }

    public void next() {
        if (!granted) return;

        checkNext();

        if (!granted) return;

        final Message message = Message.obtain();
        message.what = AnnouncificationHandler.WHAT_NEXT_ITEM;
        message.obj = queue.getFirst().getNext();
        handler.sendMessage(message);
    }

    public void onUtteranceCompleted(final String utteranceId) {
        next();
    }

    public void putFirst(final PluginQueue little) {
        queue.add(0, little);

        grant();
    }

    public void putLast(final PluginQueue little) {
        Log.d("Announcify", "Size: " + queue.size());

        queue.add(little);

        if (queue.size() == 1) {
            grant();
        }
    }

    public void quit() {
        deny();

        handler.sendEmptyMessage(AnnouncificationHandler.WHAT_SHUTDOWN);

        WakeLocker.unlock();
    }

    public void start() {
        started = true;
        grant();
    }
}
