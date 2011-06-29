package com.announcify.android.engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

import com.announcify.android.Settings;
import com.announcify.android.auth.AppEngineAuthenticator;

public class EngineWrapper {

    public static final String BASE_URL = "https://0.announcify.appspot.com/";


    public static boolean send(Context context, String event, Map<String, String> parameters) throws IOException {
        // http://www.androidsnippets.org/snippets/36/

        final HttpClient client = new DefaultHttpClient();
        final HttpPost request = new HttpPost(BASE_URL);

        final List<NameValuePair> params = new ArrayList<NameValuePair>();

        for (String key : parameters.keySet()) {
            params.add(new BasicNameValuePair(key, parameters.get(key)));
        }
        request.setEntity(new UrlEncodedFormEntity(params));

        authenticate(context, request);

        HttpResponse response = client.execute(request);
        if (response.getStatusLine().getStatusCode() == 200) {
            return true;
        } else {
            Settings.setLastError(context, EngineWrapper.class.getSimpleName() + ";" + response.getStatusLine().getStatusCode() + ";" + response.getStatusLine().getReasonPhrase());

            // TODO: show notification in notificationbar: "start announcify to fix the problem now"
        }

        return false;
    }


    private static void authenticate(Context context, HttpPost request) {
        AccountManager manager = AccountManager.get(context);
        Account account = Settings.getAccount(context, manager);

        AppEngineAuthenticator authenticator = new AppEngineAuthenticator(context, manager, account);
        authenticator.signRequest(request);
    }
}
