package com.announcify.android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class C2DMReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("com.google.android.c2dm.intent.REGISTRATION")) {
            handleRegistration(context, intent);
        } else if (intent.getAction().equals("com.google.android.c2dm.intent.RECEIVE")) {
            handleMessage(context, intent);
        }
    }


    private void handleMessage(Context context, Intent intent) {
        String message = intent.getExtras().getString("message");

        // TODO: dispatch to a service
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    private void handleRegistration(Context context, Intent intent) {
        String registration = intent.getStringExtra("registration_id");
        String error = intent.getStringExtra("error");

        if (error != null) {
            if (error.equals("ACCOUNT_MISSING")) {
                Toast.makeText(context, "You don't have a Google account set up on your phone.", Toast.LENGTH_LONG).show();
            } else if (error.equals("TOO_MANY_REGISTRATIONS")) {
                Toast.makeText(context, "You have too many apps installed on your phone, using Google's C2DM service.", Toast.LENGTH_LONG).show();
            } else if (error.equals("PHONE_REGISTRATION_ERROR")) {
                Toast.makeText(context, "Phone not supported by Google's C2DM service.", Toast.LENGTH_LONG).show();
            } else {
                // TODO: retry with backoff
            }
        } else if (intent.getStringExtra("unregistered") != null) {
        } else if (registration != null) {
            // new Auth(context, registration);
        }
    }
}
