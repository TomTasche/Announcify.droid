package com.announcify.android.auth;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.AsyncTask;

import com.announcify.android.engine.EngineWrapper;

public class C2DMAuthenticator extends Authenticator {

    final static String TYPE = "ac2dm";


    AppEngineAuthenticator appengine;

    String registration;

    AuthenticationCallback callback;


    public C2DMAuthenticator(Context context, AccountManager manager, Account account, AppEngineAuthenticator appengine, String registration) {
        super(context, manager, account, Authenticator.getPreferences(context, TYPE));

        this.appengine = appengine;
        this.registration = registration;
    }


    @Override
    public void doGenerate(AuthenticationCallback callback) {
        generateToken(account, TYPE, callback);        
    }

    @Override
    public void doSomething(AuthenticationCallback callback) {
        this.callback = callback;

        new AuthenticationTask().execute(getToken());
    }


    private class AuthenticationTask extends AsyncTask<String, Object, Boolean> {

        protected Boolean doInBackground(String... tokens) {
            String token = tokens[0];

            try {
                HashMap<String, String> parameters = new HashMap<String, String>();
                parameters.put("regId", registration);
                parameters.put("token", token);
                
                return EngineWrapper.send(context, TYPE, parameters);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Boolean result) {
            // TODO: if result ...
            
            callback.run();
        }
    }
}
