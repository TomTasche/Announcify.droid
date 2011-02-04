
package com.announcify.ui.activity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.announcify.R;
import com.announcify.api.ui.activity.BaseActivity;

public class HelpActivity extends BaseActivity {

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle, R.layout.activity_help);

        final WebView help = (WebView)findViewById(R.id.web_help);

        final WebSettings settings = help.getSettings();
        settings.setBuiltInZoomControls(true);
        settings.setLightTouchEnabled(true);
        settings.setSupportZoom(true);
        settings.setPluginsEnabled(false);
        settings.setDefaultTextEncodingName("utf-8");

        help.loadUrl("http://help.announcify.com/");
    }
}
