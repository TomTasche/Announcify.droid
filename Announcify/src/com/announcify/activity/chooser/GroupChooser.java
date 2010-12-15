package com.announcify.activity.chooser;

import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract.Groups;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.SimpleCursorAdapter;

import com.announcify.R;
import com.announcify.contact.Filter;

public class GroupChooser extends ListActivity {
	private SharedPreferences preferences;

	private GroupAdapter adapter;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_layout);

		final Cursor cursor = managedQuery(Groups.CONTENT_URI, null, null, null, null);

		findViewById(R.id.replaceEdit).setVisibility(View.GONE);
		findViewById(R.id.withLayout).setVisibility(View.GONE);

		preferences = getSharedPreferences(Filter.PREFERENCE_GROUP, Context.MODE_WORLD_READABLE);

		adapter = new GroupAdapter(cursor);
		setListAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		super.onCreateOptionsMenu(menu);

		getMenuInflater().inflate(R.menu.menu_choose, menu);
		menu.removeItem(R.id.menu_remove);
		menu.removeItem(R.id.menu_add);

		return true;
	}

	@Override
	public boolean onMenuItemSelected(final int featureId, final MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_add_all: {
				adapter.addAll();
				break;
			}

			case R.id.menu_remove_all: {
				adapter.removeAll();
				break;
			}
		}

		adapter.notifyDataSetChanged();
		return super.onMenuItemSelected(featureId, item);
	}

	private class GroupAdapter extends SimpleCursorAdapter {
		private final Cursor cursor;

		public GroupAdapter(final Cursor cursor) {
			super(GroupChooser.this, R.layout.choose_row, cursor, new String[] {Groups.TITLE}, new int[] {R.id.textLeft});
			this.cursor = cursor;
		}

		@Override
		public View getView(final int position, final View convertView, final ViewGroup parent) {
			final View view = super.getView(position, convertView, parent);
			view.findViewById(R.id.textRight).setVisibility(View.GONE);
			view.findViewById(R.id.textMiddle).setVisibility(View.GONE);
			view.findViewById(R.id.textLeft).setOnClickListener(new OnClickListener() {
				public void onClick(final View v) {
					cursor.moveToPosition(position);

					final SharedPreferences.Editor editor = preferences.edit();
					if (((CheckedTextView) v).isChecked()) {
						editor.remove((String) ((CheckedTextView) v).getText());
					} else {
						editor.putInt((String) ((CheckedTextView) v).getText(), cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID)));
					}
					editor.commit();

					((CheckedTextView) v).toggle();
				}
			});

			final CheckedTextView checkedTextView = (CheckedTextView) view.findViewById(R.id.textLeft);
			checkedTextView.setChecked(preferences.contains((String) checkedTextView.getText()));

			return view;
		}

		public void addAll() {
			final SharedPreferences.Editor editor = preferences.edit();
			cursor.moveToFirst();

			do {
				editor.putString(cursor.getString(cursor.getColumnIndex(Groups.TITLE)), "");
			} while (cursor.moveToNext());

			editor.commit();
		}

		public void removeAll() {
			final SharedPreferences.Editor editor = preferences.edit();
			editor.clear();
			editor.commit();
		}
	}
}
