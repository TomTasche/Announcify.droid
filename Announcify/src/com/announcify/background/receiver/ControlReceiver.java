
package com.announcify.background.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Message;

import com.announcify.background.handler.AnnouncificationHandler;
import com.announcify.ui.control.RemoteControlDialog;

public class ControlReceiver extends BroadcastReceiver {
    private final AnnouncificationHandler handler;

    public ControlReceiver(final AnnouncificationHandler handler) {
        this.handler = handler;
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        final Message msg = Message.obtain();
        if (RemoteControlDialog.ACTION_CONTINUE.equals(intent.getAction())) {
            msg.what = AnnouncificationHandler.WHAT_CONTINUE;
            handler.sendMessage(msg);
        } else if (RemoteControlDialog.ACTION_SKIP.equals(intent.getAction())) {
            msg.what = AnnouncificationHandler.WHAT_PAUSE;
            handler.sendMessage(msg);
            msg.what = AnnouncificationHandler.WHAT_CONTINUE;
            handler.sendMessage(msg);
        } else if (RemoteControlDialog.ACTION_PAUSE.equals(intent.getAction())) {
            msg.what = AnnouncificationHandler.WHAT_PAUSE;
            handler.sendMessage(msg);
        }
    }
}
