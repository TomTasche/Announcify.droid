package com.announcify.plugin.talk.google.contact;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.provider.ContactsContract.Data;

import com.announcify.api.background.contact.Contact;
import com.announcify.api.background.contact.lookup.LookupMethod;

public class Chat implements LookupMethod {

    private final Context context;
    private Contact contact;

    public Chat(final Context context) {
        this.context = context;
    }

    public void getAddress() {
        Cursor cursor = null;

        try {
            cursor = context.getContentResolver().query(
                    Data.CONTENT_URI,
                    new String[] { Im.DATA1 },
                    Im.LOOKUP_KEY + " = ? AND " + Im.MIMETYPE + " = ?",
                    new String[] { contact.getLookupString(),
                            Im.CONTENT_ITEM_TYPE }, null);
            if (!cursor.moveToFirst()) {
                return;
            }

            // TODO: implement?
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void getLookup(final Contact contact) {
        this.contact = contact;

        Cursor cursor = null;

        try {
            cursor = context.getContentResolver().query(Data.CONTENT_URI,
                    new String[] { Im.LOOKUP_KEY },
                    // Im.DATA1 + " = ? AND " + Im.MIMETYPE + " = ?",
                    Im.DATA1 + " = ?", new String[] { contact.getAddress() },
                    null);
            if (!cursor.moveToFirst()) {
                return;
            }

            contact.setLookupString(cursor.getString(cursor
                    .getColumnIndex(Im.LOOKUP_KEY)));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void getType() {
        Cursor cursor = null;

        try {
            cursor = context.getContentResolver().query(
                    Data.CONTENT_URI,
                    new String[] { Im.TYPE },
                    Im.LOOKUP_KEY + " = ? AND " + Data.MIMETYPE + " = ? AND "
                            + Im.DATA1 + " = ?",
                    new String[] { contact.getLookupString(),
                            Im.CONTENT_ITEM_TYPE, contact.getAddress() }, null);
            if (!cursor.moveToFirst()) {
                return;
            }

            String label = cursor.getString(cursor.getColumnIndex(Im.LABEL));
            if (label == null) {
                label = Resources.getSystem().getStringArray(
                        android.R.array.imProtocols)[cursor.getInt(cursor
                        .getColumnIndex(Im.TYPE)) - 1];
            }
            contact.setType(label);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
