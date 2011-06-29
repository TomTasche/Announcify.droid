package com.announcify.android;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Settings {
    
    public static final String KEY_ACCOUNT = "account";
    public static final String KEY_LAST_ERROR = "last_error";
    

    private static SharedPreferences preferences;
    
    private static void createPreferences(Context context) {
        preferences = context.getSharedPreferences("com.announcify", Context.MODE_PRIVATE);
    }
    
    public static SharedPreferences getPreferences(Context context) {
        if (preferences == null) createPreferences(context);
        return preferences;
    }
    
    
    public static Account getAccount(Context context, AccountManager manager) {
        String savedAccount = getPreferences(context).getString(KEY_ACCOUNT, "");
        
        Account[] accounts = manager.getAccounts();
        for (Account account : accounts) {
            if (savedAccount.equals(account.name)) return account;
        }
        
        return null;
    }
    
    
    public static String getLastError(Context context) {
        return getPreferences(context).getString(KEY_LAST_ERROR, null);
    }
    
    public static void setLastError(Context context, String error) {
        Editor editor = getPreferences(context).edit();
        editor.putString(KEY_LAST_ERROR, error);
        editor.commit();
    }
}
