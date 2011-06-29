package com.announcify.android.auth;

import java.io.IOException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

/**
 * http://blog.notdot.net/2010/05/Authenticating-against-App-Engine-from-an-Android-app
 * http://codesearch.google.com/codesearch/p?hl=en#JWblrwroAxw/trunk/android/src/com/google/android/apps/chrometophone/AppEngineClient.java&q=/_ah/login%20lang:java&d=8
 * 
 * @author Tom Tasche
 *
 */
public abstract class Authenticator {
    
    static final String KEY_TOKEN = "token";

    public static SharedPreferences getPreferences(Context context, String name) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }
    
    
    AccountManager manager;
    Account account;

    SharedPreferences preferences;
    Context context;
    
    boolean authenticated;


    public Authenticator(Context context, AccountManager manager, Account account, SharedPreferences preferences) {
        this.context = context;
        this.manager = manager;
        this.account = account;
        this.preferences = preferences;
    }

    
    public void generateToken(final Account account, final String service, AuthenticationCallback callback) {
        String token = getToken();
        if (token != null) manager.invalidateAuthToken(service, token);

        manager.getAuthToken(account, service, false, new TokenFuture(callback), null);
    }
    
    public abstract void doGenerate(AuthenticationCallback callback);
    
    public abstract void doSomething(AuthenticationCallback callback);
    
    public String getToken() {
        return preferences.getString(KEY_TOKEN, null);
    }
    
    public void setToken(String token) {
        Editor editor = preferences.edit();
        editor.putString(KEY_TOKEN, token);
        editor.commit();
    }

    public boolean isAuthenticated() {
        return authenticated;
    }
    
    
    public interface AuthenticationCallback {
        
        public void run();
    }
    
    
    private class TokenFuture implements AccountManagerCallback<Bundle> {
        
        AuthenticationCallback callback;
        
        
        public TokenFuture(AuthenticationCallback callback) {
            this.callback = callback;
        }
        
        
        @Override
        public void run(AccountManagerFuture<Bundle> future) {
            try {
                Bundle bundle = (Bundle) future.getResult();
                Intent intent = (Intent) bundle.get(AccountManager.KEY_INTENT);
                
                if (intent != null) {
                    context.startActivity(intent);
                } else {
                    String token = bundle.getString(AccountManager.KEY_AUTHTOKEN);
                    setToken(token);
                    
                    doSomething(callback);
                    
                    authenticated = true;
                }
            } catch (OperationCanceledException e) {
                e.printStackTrace();
            } catch (AuthenticatorException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}