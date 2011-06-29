package com.announcify.android.service;

import java.io.IOException;
import java.util.HashMap;

import android.content.Intent;
import android.telephony.TelephonyManager;

import com.announcify.android.engine.EngineWrapper;

public class CallService extends EventService {

    public static final String EVENT = "call";
    
    static final String EXTRA_NUMBER = TelephonyManager.EXTRA_INCOMING_NUMBER;
    static final String EXTRA_STATE = TelephonyManager.EXTRA_STATE;


    public CallService() {
        super(CallService.class.getSimpleName());
    }


    @Override
    protected void doWakefulWork(Intent intent) {
        HashMap<String, String> parameters = new HashMap<String, String>();
        
        String number = intent.getStringExtra(EXTRA_NUMBER);
        if (number == null) number = "";
        parameters.put(KEY_NUMBER, number);

        String state = intent.getStringExtra(EXTRA_STATE);
        if (state == null) state = "";
        parameters.put(KEY_STATE, state);

        try {
            EngineWrapper.send(this, EVENT, parameters);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
