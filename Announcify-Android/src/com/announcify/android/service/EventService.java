package com.announcify.android.service;

import android.content.Intent;

import com.commonsware.cwac.wakeful.WakefulIntentService;

public abstract class EventService extends WakefulIntentService {

    public static final String KEY_NUMBER = "number";
    public static final String KEY_STATE = "state";
    
    
    public EventService(String name) {
        super("Announcify - " + name);
    }

    
    @Override
    protected abstract void doWakefulWork(Intent intent);
}
