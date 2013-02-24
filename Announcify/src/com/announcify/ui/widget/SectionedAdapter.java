package com.announcify.ui.widget;

import org.mailboxer.saymyname.R;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.provider.BaseColumns;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AlphabetIndexer;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.announcify.api.background.sql.model.PluginModel;
import com.mobfox.sdk.MobFoxView;
import com.mobfox.sdk.Mode;

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
			if (view.findViewById(R.id.admob) == null) {
				view.findViewById(R.id.separator).setVisibility(View.GONE);
				view.findViewById(R.id.icon).setVisibility(View.GONE);
				view.findViewById(R.id.settings).setVisibility(View.GONE);
				view.findViewById(R.id.check).setVisibility(View.GONE);
				view.findViewById(R.id.plugin_info).setVisibility(View.GONE);

				String publisherId = "301e9eaa45fe169aa3dbd5125b6b827a";
				boolean includeLocation = false;
				Mode mode = Mode.LIVE;
				boolean animation = true;
				MobFoxView mobfoxView = new MobFoxView(context, publisherId,
						mode, includeLocation, animation);
				mobfoxView.setId(R.id.admob);

				((LinearLayout) view).addView(mobfoxView);
			} else {
				view.findViewById(R.id.plugin_info).setVisibility(View.GONE);
				view.findViewById(R.id.separator).setVisibility(View.GONE);
				view.findViewById(R.id.icon).setVisibility(View.GONE);
				view.findViewById(R.id.admob).setVisibility(View.VISIBLE);
				view.findViewById(R.id.settings).setVisibility(View.GONE);
				view.findViewById(R.id.plugin).setVisibility(View.GONE);
				view.findViewById(R.id.check).setVisibility(View.GONE);
			}

			return;
		}

		view.findViewById(R.id.icon).setVisibility(View.VISIBLE);
		view.findViewById(R.id.separator).setVisibility(View.VISIBLE);
		view.findViewById(R.id.settings).setVisibility(View.VISIBLE);
		view.findViewById(R.id.check).setVisibility(View.VISIBLE);
		view.findViewById(R.id.plugin).setVisibility(View.VISIBLE);
		if (view.findViewById(R.id.admob) != null) {
			View mobfox = view.findViewById(R.id.admob);
			mobfox.setVisibility(View.GONE);
			mobfox = null;
		}
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

		// view.setOnClickListener(new OnClickListener() {
		//
		// public void onClick(final View v) {
		// context.startActivity(new Intent(model.getAction((Long)
		// v.getTag())));
		// }
		// });
	}

	@Override
	public View getView(final int position, View convertView,
			final ViewGroup parent) {
		convertView = super.getView(position, convertView, parent);

		final int section = indexer.getSectionForPosition(position);

		final TextView sectionView = (TextView) convertView
				.findViewById(R.id.section);
		if (indexer.getPositionForSection(section) == position) {
			SpannableString content = new SpannableString(
					indexer.getSections()[section].toString().trim());
			content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
			sectionView.setText(content);
			sectionView.setTextColor(convertView.getResources().getColor(
					R.color.highlight_color));
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
		return inflater.inflate(R.layout.list_item_plugin, parent, false);
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();

		indexer.onChanged();
	}
}
