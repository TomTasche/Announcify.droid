
package com.announcify.plugin.talk.google.contact;

import android.content.Context;

import com.announcify.api.contact.Contact;
import com.announcify.api.contact.lookup.LookupMethod;

public class Twitter implements LookupMethod {

    private final Context context;

    private Contact contact;

    public Twitter(final Context context) {
        this.context = context;
    }

    public void getLookup(final Contact contact) {
        // TODO
    }

    public void getType() {
        // TODO Auto-generated method stub

    }

    public void getAddress() {
        // TODO Auto-generated method stub

    }

}
