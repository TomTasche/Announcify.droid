
package com.announcify.ui.activity;

import greendroid.app.GDActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ActionBarItem.Type;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.announcify.R;

public class BaseActivity extends GDActivity {

    public BaseActivity() {
        super(greendroid.widget.ActionBar.Type.Dashboard);
    }

    @Override
    protected void onCreate(final Bundle bundle) {
        setTheme(R.style.Theme_Announcify);

        super.onCreate(bundle);

        // final ImageButton button =
        // (ImageButton)findViewById(R.id.gd_action_bar_home_item);
        // button.setImageDrawable(getResources().getDrawable(R.drawable.launcher_icon));
        // button.setOnClickListener(new OnClickListener() {
        //
        // public void onClick(final View v) {
        // final Intent announcify = new Intent("com.announcify");
        // announcify.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        // startActivity(announcify);
        // }
        // });

        // addActionBarItem(Type.SortBySize);
        addActionBarItem(Type.Talk);
        addActionBarItem(Type.Add);
        addActionBarItem(Type.Share);
    }

    protected void spreadTheWord() {
        final Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent
                .putExtra(Intent.EXTRA_TEXT,
                        "I'm in love with my phone, now that it's talking to me!\n@Announcify - http://announcify.com");
        shareIntent.setType("text/plain");
        shareIntent.addCategory(Intent.CATEGORY_DEFAULT);
        startActivity(shareIntent);
    }

    protected void morePlugins() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://plugins.announcify.com/")));
        // market://search?q=Announcify
    }

    @Override
    public boolean onHandleActionBarItemClick(final ActionBarItem item, final int position) {
        switch (position) {
            case 0:
                // adapter.toggleSort();

                startActivity(new Intent(this, HelpActivity.class));

                break;

            case 1:
                morePlugins();
                break;

            case 2:
                spreadTheWord();
                break;

            default:
                return super.onHandleActionBarItemClick(item, position);
        }

        return true;
    }
}
