package com.announcify.api.ui.activity;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;

public class BaseActivity extends SherlockActivity {

    protected void onCreate(final Bundle savedInstanceState, final int layoutResId) {
        super.onCreate(savedInstanceState);

        setContentView(layoutResId);
    }
}
