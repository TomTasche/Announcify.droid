package com.announcify.plugin.message.service;

import java.util.LinkedList;

import android.content.Intent;
import android.telephony.SmsMessage;
import android.util.Log;

import com.announcify.contact.Contact;
import com.announcify.contact.Lookup;
import com.announcify.plugin.message.receiver.RingtoneReceiver;
import com.announcify.queue.LittleQueue;
import com.announcify.service.AnnouncifyService;

public class WorkerService extends AnnouncifyService {
	public WorkerService() {
		super("Announcify - Message");
	}

	@Override
	protected void onHandleIntent(final Intent intent) {
		final Object[] pdusObj = (Object[]) intent.getExtras().get("pdus");

		final SmsMessage[] messages = new SmsMessage[pdusObj.length];
		for (int i = 0; i < pdusObj.length; i++) {
			messages[i] = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
		}

		String temp = "";

		if (messages.length > 1) {
			for (final SmsMessage currentMessage : messages) {
				temp = temp + currentMessage.getDisplayMessageBody() + '\n';
			}
		} else {
			temp = messages[0].getDisplayMessageBody();
		}

		final String number = messages[0].getDisplayOriginatingAddress();
		final String message = temp;

		Log.e("smn", number);
		final Contact contact = new Contact(this, number);
		if (number != null && !"".equals(number)) {
			Lookup.lookupNumber(contact);
			Lookup.getNickname(contact);
		}

		final String name = contact.getUserPreferredName();

		Log.e("smn", name);
		final LinkedList<Object> list = new LinkedList<Object>();
		list.add(name);

		final LittleQueue queue = new LittleQueue("Messnouncify", list, "", RingtoneReceiver.ACTION_STOP_RINGTONE, this);

		final Intent announceIntent = new Intent(ACTION_ANNOUNCE);
		announceIntent.putExtra(EXTRA_QUEUE, queue);
		announceIntent.putExtra(EXTRA_PRIORITY, 1);
		startService(announceIntent);
	}
}
