package com.announcify.ui.widget;

import org.mailboxer.saymyname.R;

import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AlphabetIndexer;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.announcify.api.background.sql.model.PluginModel;
import com.emilsjolander.components.stickylistheaders.StickyListHeadersAdapter;

public class StickyListBaseAdapter extends CursorAdapter implements
		StickyListHeadersAdapter {

	private static final String SECTIONS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

	private final AlphabetIndexer indexer;
	private final LayoutInflater inflater;
	private final PluginModel model;

	private final int idIndex;

	public StickyListBaseAdapter(final Context context,
			final PluginModel model, final Cursor cursor) {
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

	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		HeaderViewHolder holder;
		if (convertView == null) {
			holder = new HeaderViewHolder();
			convertView = inflater.inflate(R.layout.list_row_header, parent,
					false);
			holder.text = (TextView) convertView.findViewById(R.id.headerText);
			convertView.setTag(holder);
		} else {
			holder = (HeaderViewHolder) convertView.getTag();
		}
		// set header text as first char in name
		char headerChar = indexer.getSections()[indexer
				.getSectionForPosition(position)].toString().trim().charAt(0);
		String headerText = Character.toString(headerChar);
		holder.text.setText(headerText);
		return convertView;
	}

	class HeaderViewHolder {
		TextView text;
	}

	public long getHeaderId(int position) {
		final int section = indexer.getSectionForPosition(position);

		return indexer.getSections()[section].toString().trim().charAt(0);
	}
}
