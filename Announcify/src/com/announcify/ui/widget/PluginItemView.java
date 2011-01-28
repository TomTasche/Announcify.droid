
package com.announcify.ui.widget;

import greendroid.widget.item.Item;
import greendroid.widget.itemview.ItemView;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.announcify.R;

public class PluginItemView extends LinearLayout implements ItemView {

    protected final Context context;

    protected TextView headerView;

    protected ImageView icon;

    protected TextView settings;

    protected ImageView check;

    public PluginItemView(final Context context) {
        this(context, null);
    }

    public PluginItemView(final Context context, final AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
    }

    public void prepareItemView() {
        headerView = (TextView)findViewById(R.id.gd_separator_text);
        headerView.setBackgroundColor(Color.parseColor("#AD0000"));

        icon = (ImageView)findViewById(R.id.icon);
        settings = (TextView)findViewById(R.id.settings);
        check = (ImageView)findViewById(R.id.check);
    }

    public void setObject(final Item object) {
        final PluginItem item = (PluginItem)object;

        final String headerString = item.header;

        if (!TextUtils.isEmpty(headerString)) {
            headerView.setText(headerString);
            headerView.setVisibility(View.VISIBLE);
        } else {
            headerView.setVisibility(View.GONE);
        }

        try {
            icon.setImageDrawable(item.getDrawable());
        } catch (final NameNotFoundException e) {
            e.printStackTrace();
        }

        settings.setText(item.getName());

        check.setImageDrawable(getResources().getDrawable(
                item.getActive() ? R.drawable.btn_check_buttonless_on
                        : R.drawable.btn_check_buttonless_off));
        check.setOnClickListener(new OnClickListener() {

            public void onClick(final View v) {
                item.toggle();

                check.setImageDrawable(getResources().getDrawable(
                        item.getActive() ? R.drawable.btn_check_buttonless_on
                                : R.drawable.btn_check_buttonless_off));
            }
        });
    }

    public String getText() {
        return settings.getText().toString();
    }
}
