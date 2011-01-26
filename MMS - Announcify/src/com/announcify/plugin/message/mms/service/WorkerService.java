
package com.announcify.plugin.message.mms.service;

import android.content.Intent;

import com.announcify.api.AnnouncifyIntent;
import com.announcify.api.contact.Contact;
import com.announcify.api.contact.Filter;
import com.announcify.api.contact.lookup.Number;
import com.announcify.api.error.ExceptionHandler;
import com.announcify.api.service.PluginService;
import com.announcify.api.text.Formatter;
import com.announcify.plugin.message.mms.receiver.RingtoneReceiver;
import com.announcify.plugin.message.mms.util.Settings;
import com.announcify.plugin.message.mms.util.pdu.EncodedStringValue;
import com.announcify.plugin.message.mms.util.pdu.PduHeaders;
import com.announcify.plugin.message.mms.util.pdu.PduParser;

public class WorkerService extends PluginService {

    public WorkerService() {
        super("Announcify - Message");
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this, Thread
                .getDefaultUncaughtExceptionHandler()));

        String number = null;

        if (intent.getExtras().containsKey("data")) {
            final PduParser parser = new PduParser();
            final PduHeaders headers = parser.parseHeaders(intent.getByteArrayExtra("data"));

            if (headers == null) {
                return;
            }

            if (headers.getMessageType() == PduHeaders.MESSAGE_TYPE_NOTIFICATION_IND) {
                final EncodedStringValue from = headers.getFrom();
                if (from != null) {
                    number = from.getString();
                } else {
                    return;
                }
            }
        } else {
            number = intent.getStringExtra("com.announcify.EXTRA_TEST");
        }

        final Settings settings = new Settings(this);

        if (number == null && "".equals(number)) {
            return;
        }
        final Contact contact = new Contact(this, new Number(this), number);
        
        if (!Filter.announcable(this, contact)) return;

        final Formatter formatter = new Formatter(this, contact, settings);

        final AnnouncifyIntent announcify = new AnnouncifyIntent(this, settings);
        announcify.setStopBroadcast(RingtoneReceiver.ACTION_START_RINGTONE);
        announcify.announce(formatter.format(intent.getStringExtra("Multimedia Message")));
    }
}
