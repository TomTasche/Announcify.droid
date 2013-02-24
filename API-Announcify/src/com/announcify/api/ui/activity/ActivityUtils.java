package com.announcify.api.ui.activity;

import android.content.Context;
import android.content.Intent;

import com.announcify.api.R;

public class ActivityUtils {
    public static Intent getHomeIntent() {
        final Intent intent = new Intent("com.announcify");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    public static Intent getShareIntent(final Context context) {
        final Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.string_share_announcify));
        shareIntent.setType("text/plain");
        shareIntent.addCategory(Intent.CATEGORY_DEFAULT);
        return shareIntent;
    }
}
