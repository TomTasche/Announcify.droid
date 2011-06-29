package com.announcify.android.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.announcify.android.R;
import com.announcify.android.auth.AppEngineAuthenticator;
import com.announcify.android.auth.Authenticator;
import com.announcify.android.auth.Authenticator.AuthenticationCallback;

public class SetupActivity extends ListActivity {

    AccountManager manager;
    Account account;

    Authenticator appengine;
    Authenticator c2dm;

    AppEngineAuthenticationCallback appengineCallback = new AppEngineAuthenticationCallback();
    C2DMAuthenticationCallback c2dmCallback = new C2DMAuthenticationCallback();

    C2DMRegistrationReceiver registrationReceiver;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.main);

        // TODO: Settings.getLastError(this);

        manager = AccountManager.get(this);
        Account[] accounts = manager.getAccountsByType("com.google");
        this.setListAdapter(new ArrayAdapter<Account>(this, android.R.layout.simple_list_item_1, accounts));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        account = (Account) this.getListView().getItemAtPosition(position);

        appengine = new AppEngineAuthenticator(this, manager, account);
        c2dm = new AppEngineAuthenticator(this, manager, account);

        appengine.doGenerate(appengineCallback);

        registerForRegistration();
    }

    @Override
    protected void onResume() {
        registerForRegistration();

        if (!appengine.isAuthenticated()) {
            if (appengine.getToken() != null) {
                appengine.doSomething(appengineCallback);
            } else {
                appengine.doGenerate(appengineCallback);
            }
        } else if (!c2dm.isAuthenticated()) {
            c2dm.doSomething(c2dmCallback);
        }
    }

    @Override
    protected void onPause() {
        if (registrationReceiver != null) unregisterReceiver(registrationReceiver);

        super.onPause();
    }


    private void registerForRegistration() {
        if (c2dm.getToken() == null) {
            registrationReceiver = new C2DMRegistrationReceiver();

            IntentFilter filter = new IntentFilter("com.google.android.c2dm.intent.REGISTRATION");
            filter.addCategory("com.announcify.android");

            registerReceiver(registrationReceiver, filter);
        } else {
            registrationReceiver = null;
        }
    }


    private class AppEngineAuthenticationCallback implements AuthenticationCallback {

        @Override
        public void run() {
            c2dm.doGenerate(c2dmCallback);
        }
    }

    private class C2DMAuthenticationCallback implements AuthenticationCallback {

        @Override
        public void run() {
            // TODO: close activity + remove activity from launcher?
            Toast.makeText(SetupActivity.this, "You are now logged in.", Toast.LENGTH_LONG).show();
        }
    }


    private class C2DMRegistrationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub

        }
    }
}