package com.announcify.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AlphabetIndexer;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.announcify.R;
import com.announcify.api.background.sql.model.PluginModel;
import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.AdSize;
import com.google.ads.AdView;


public class SectionedAdapter extends CursorAdapter {

    private static final String SECTIONS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private final AlphabetIndexer indexer;
    private final LayoutInflater inflater;
    private final PluginModel model;

    private final int idIndex;

    public SectionedAdapter(final Context context, final PluginModel model,
            final Cursor cursor) {
        super(context, cursor);

        this.model = model;
        inflater = LayoutInflater.from(context);
        indexer = new AlphabetIndexer(getCursor(),
                cursor.getColumnIndexOrThrow(PluginModel.KEY_PLUGIN_NAME),
                SECTIONS);

        idIndex = cursor.getColumnIndexOrThrow(BaseColumns._ID);
    }

    @Override
    public void bindView(final View view, final Context context,
            final Cursor cursor) {
        final long id = cursor.getLong(idIndex);
        view.setTag(id);

        if ("BbAdMob".equals(model.getName(id))) {
            ((TextView) view.findViewById(R.id.plugin)).setText("Publisher? Display your ad here!");

            view.findViewById(R.id.separator).setVisibility(View.GONE);
            view.findViewById(R.id.settings).setVisibility(View.GONE);

            view.findViewById(R.id.plugin).setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    final Intent sendIntent = new Intent(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Announcify - Advertisement");
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "");
                    sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "tom@announcify.com" });
                    sendIntent.setType("message/rfc822");
                    context.startActivity(sendIntent);
                }
            });

            final AdRequest ad = new AdRequest();
            ad.setTesting(true);

            final AdView adView = new AdView((Activity) context, AdSize.BANNER, "a14d81b0baa0b54");
            ((LinearLayout) view).addView(adView);
            adView.setAdListener(new AdListener() {

                public void onReceiveAd(Ad arg0) {
                    view.findViewById(R.id.plugin_info).setVisibility(View.GONE);
                }

                public void onPresentScreen(Ad arg0) {
                    ((TextView) view.findViewById(R.id.plugin)).setText("Hate ads? Purchase Pro.");
                    view.findViewById(R.id.plugin).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.plugin_info).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.plugin).setOnClickListener(new OnClickListener() {

                        public void onClick(View v) {
                            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://goo.gl/p4jH2")));
                        }
                    });
                }

                public void onLeaveApplication(Ad arg0) {}

                public void onFailedToReceiveAd(Ad arg0, ErrorCode arg1) {}

                public void onDismissScreen(Ad arg0) {}
            });
            adView.loadAd(ad);

            return;
        }

        view.findViewById(R.id.separator).setVisibility(View.VISIBLE);
        view.findViewById(R.id.settings).setVisibility(View.VISIBLE);
        view.findViewById(R.id.plugin_info).setVisibility(View.VISIBLE);

        ((ImageView) view.findViewById(R.id.icon)).setImageDrawable(context
                .getResources().getDrawable(R.drawable.launcher_icon));

        ((TextView) view.findViewById(R.id.plugin)).setText(model.getName(id));

        final ImageView check = (ImageView) view.findViewById(R.id.check);
        check.setTag(id);
        check.setImageDrawable(context.getResources().getDrawable(
                model.getActive(id) ? R.drawable.btn_check_buttonless_on
                        : R.drawable.btn_check_buttonless_off));
        check.setOnClickListener(new OnClickListener() {

            public void onClick(final View v) {
                model.togglePlugin((Long) v.getTag());

                check.setImageDrawable(context
                        .getResources()
                        .getDrawable(
                                model.getActive(id) ? R.drawable.btn_check_buttonless_on
                                        : R.drawable.btn_check_buttonless_off));
            }
        });
    }

    @Override
    public View getView(final int position, View convertView,
            final ViewGroup parent) {
        convertView = super.getView(position, convertView, parent);

        final int section = indexer.getSectionForPosition(position);

        final TextView sectionView = (TextView) convertView
        .findViewById(R.id.section);
        if (indexer.getPositionForSection(section) == position) {
            sectionView.setBackgroundColor(Color.parseColor("#AD0000"));
            sectionView.setText(indexer.getSections()[section].toString()
                    .trim());
            sectionView.setVisibility(View.VISIBLE);
        } else {
            sectionView.setVisibility(View.GONE);
            sectionView.setText(null);
        }

        return convertView;
    }

    @Override
    public View newView(final Context context, final Cursor cursor,
            final ViewGroup parent) {
        return inflater.inflate(com.announcify.R.layout.list_item_plugin,
                parent, false);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();

        indexer.onChanged();
    }
}
