package com.announcify.plugin.mail.service;

import java.util.LinkedList;

import android.content.Intent;
import android.util.Log;

import com.announcify.contact.Contact;
import com.announcify.contact.Lookup;
import com.announcify.plugin.mail.receiver.RingtoneReceiver;
import com.announcify.plugin.mail.util.MailnouncifySettings;
import com.announcify.queue.LittleQueue;
import com.announcify.queue.Prepare;
import com.announcify.service.AnnouncifyService;

public class WorkerService extends AnnouncifyService {
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

		MailnouncifySettings settings = new MailnouncifySettings(this);

		Prepare prepare = new Prepare(this, settings, contact, intent.getStringExtra(EXTRA_SUBJECT));
		final LinkedList<Object> list = new LinkedList<Object>();
		prepare.getQueue(list);

		if (list.isEmpty()) return;

		final LittleQueue queue = new LittleQueue("Mailnouncify", list, "", RingtoneReceiver.ACTION_STOP_RINGTONE, this);
		queue.sendToService(this, 2);
	}
}
