package com.announcify.ui.activity;

import java.util.Locale;

import org.mailboxer.saymyname.R;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract.Contacts;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.CheckedTextView;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.CursorToStringConverter;
import android.widget.TextView;

import com.announcify.api.background.sql.model.ContactModel;
import com.announcify.api.background.util.AnnouncifySettings;
import com.announcify.api.ui.activity.BaseActivity;

public class ContactActivity extends BaseActivity {

	protected CheckedTextView checkBlock;
	protected ListView list;
	protected AutoCompleteTextView auto;
	protected SimpleCursorAdapter listAdapter;
	protected SimpleCursorAdapter autoAdapter;
	protected ContactModel model;
	protected Cursor listCursor;
	protected Cursor autoCursor;
	protected AnnouncifySettings settings;

	@Override
	protected void onCreate(final Bundle bundle) {
		super.onCreate(bundle, R.layout.activity_chooser);

		settings = new AnnouncifySettings(this);

		checkBlock = (CheckedTextView) findViewById(R.id.check_block);

		listAdapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_1, null, null, null);

		list = (ListView) findViewById(android.R.id.list);
		list.setAdapter(listAdapter);
		list.setBackgroundColor(Color.WHITE);
		list.setCacheColorHint(Color.TRANSPARENT);
		list.setFastScrollEnabled(true);

		registerForContextMenu(list);

		autoAdapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_1, null, null, null);

		auto = (AutoCompleteTextView) findViewById(R.id.auto_edit_chooser);
		auto.setSingleLine();
		auto.setThreshold(1);
		auto.setAdapter(autoAdapter);

		auto.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(final AdapterView<?> parent,
					final View view, final int position, final long id) {
				final Cursor cursor = ((Cursor) autoAdapter.getItem(position));
				if (cursor == null) {
					return;
				}

				final String lookup = cursor.getString(autoCursor
						.getColumnIndex(Contacts.LOOKUP_KEY));
				if (lookup == null) {
					return;
				}

				model.add(lookup, ((TextView) view).getText().toString());

				refreshList();

				auto.setText("");
			}
		});

		checkBlock.setChecked(settings.getBlockContacts());
		checkBlock.setOnClickListener(new OnClickListener() {

			public void onClick(final View v) {
				final boolean enable = !checkBlock.isChecked();
				settings.setBlockContacts(enable);
				checkBlock.setChecked(enable);
			}
		});

		model = new ContactModel(this);

		listAdapter.changeCursorAndColumns(null,
				new String[] { ContactModel.KEY_CONTACT_TITLE },
				new int[] { android.R.id.text1 });

		autoAdapter.setCursorToStringConverter(new CursorToStringConverter() {

			public CharSequence convertToString(final Cursor cursor) {
				return cursor.getString(cursor
						.getColumnIndex(Contacts.DISPLAY_NAME));
			}
		});
		autoAdapter.setFilterQueryProvider(new FilterQueryProvider() {

			public Cursor runQuery(final CharSequence constraint) {
				return getContentResolver().query(
						Contacts.CONTENT_URI,
						new String[] { BaseColumns._ID, Contacts.LOOKUP_KEY,
								Contacts.DISPLAY_NAME },
						"UPPER(" + Contacts.DISPLAY_NAME + ") GLOB ?",
						new String[] { constraint.toString().toUpperCase(
								Locale.ENGLISH)
								+ "*" }, Contacts.DISPLAY_NAME);
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();

		refreshList();

		autoCursor = getContentResolver().query(
				Contacts.CONTENT_URI,
				new String[] { BaseColumns._ID, Contacts.LOOKUP_KEY,
						Contacts.DISPLAY_NAME }, null, null,
				Contacts.DISPLAY_NAME);
		autoAdapter.changeCursorAndColumns(autoCursor,
				new String[] { Contacts.DISPLAY_NAME },
				new int[] { android.R.id.text1 });
	}

	@Override
	public void onCreateContextMenu(final ContextMenu menu, final View v,
			final ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		getMenuInflater().inflate(R.menu.context_choose, menu);
	}

	@Override
	protected void onStop() {
		listCursor.close();
		autoCursor.close();

		super.onStop();
	}

	protected void refreshList() {
		if (listCursor != null) {
			listCursor.close();
		}

		listCursor = model.getAll();
		listAdapter.changeCursor(listCursor);
	}

	@Override
	public boolean onContextItemSelected(final MenuItem item) {
		final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();

		if (item.getItemId() == R.id.menu_remove) {
			model.remove(info.id);

			refreshList();
		}

		return super.onContextItemSelected(item);
	}
}
