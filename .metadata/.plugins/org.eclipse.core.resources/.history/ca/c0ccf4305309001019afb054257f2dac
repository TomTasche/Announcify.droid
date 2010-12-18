package com.announcify.plugin.mail.service;

import java.util.LinkedList;

import android.content.Intent;
import android.util.Log;

import com.announcify.contact.Contact;
import com.announcify.contact.Lookup;
import com.announcify.plugin.mail.receiver.RingtoneReceiver;
import com.announcify.queue.LittleQueue;
import com.announcify.service.AnnouncifyService;

public class WorkerService extends AnnouncifyService {
	public static final String ACTION_ANNOUNCE = "com.announcify.ANNOUNCE";

	public static final String EXTRA_FROM = "com.announcify.plugin.mail.EXTRA_FROM";
	public static final String EXTRA_SUBJECT = "com.announcify.plugin.mail.EXTRA_SUBJECT";
	public static final String EXTRA_SNIPPET = "com.announcify.plugin.mail.EXTRA_SNIPPET";

	public WorkerService() {
		super("Announcify - Mail");
	}

	@Override
	protected void onHandleIntent(final Intent intent) {
		final String address = intent.getStringExtra(EXTRA_FROM);
		Log.e("smn", address);
		final Contact contact = new Contact(this, address);
		if (address != null && !"".equals(address)) {
			Lookup.lookupMail(contact);
			Lookup.getNickname(contact);
		}

		final String name = contact.getUserPreferredName();

		Log.e("smn", name);
		final LinkedList<Object> list = new LinkedList<Object>();
		list.add(name);

		final LittleQueue queue = new LittleQueue("Mailnouncify", list, "", RingtoneReceiver.ACTION_STOP_RINGTONE, this);

		final Intent announceIntent = new Intent("com.announcify.ANNOUNCE");
		announceIntent.putExtra(EXTRA_QUEUE, queue);
		announceIntent.putExtra(EXTRA_PRIORITY, 0);
		startService(announceIntent);
	}
}
