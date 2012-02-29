package com.announcify.ui.activity;

import org.mailboxer.saymyname.R;

import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.BaseColumns;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.announcify.api.background.sql.model.PluginModel;
import com.announcify.api.ui.activity.ActivityUtils;
import com.announcify.api.ui.activity.BaseActivity;
import com.announcify.background.sql.AnnouncifyProvider;
import com.announcify.ui.widget.SectionedAdapter;

public class AnnouncifyActivity extends BaseActivity {

	private class AnnouncifyObserver extends ContentObserver {

		public AnnouncifyObserver(final Handler handler) {
			super(handler);
		}

		@Override
		public void onChange(final boolean selfChange) {
			super.onChange(selfChange);

			refreshList();
		}
	}

	private Cursor cursor;
	private PluginModel model;
	private ContentObserver observer;
	private SectionedAdapter adapter;

	private ListView getListView() {
		return (ListView) findViewById(android.R.id.list);
	}

	@Override
	protected void onActivityResult(final int requestCode,
			final int resultCode, final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode > 2000) {
			try {
				getPackageManager().getPackageGids(
						model.getPackage(requestCode - 2000));
			} catch (final NameNotFoundException e) {
				model.remove(requestCode - 2000);
			}
		}
	}

	@Override
	public boolean onContextItemSelected(final MenuItem item) {
		final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();

		switch (item.getItemId()) {
		case R.id.menu_uninstall:
			startActivityForResult(
					new Intent(Intent.ACTION_DELETE, Uri.parse("package:"
							+ model.getPackage(info.id))),
					(int) (2000 + info.id));

			break;

		case R.id.menu_activate:
			model.togglePlugin(info.id);

			adapter.notifyDataSetChanged();

			break;

		case R.id.menu_report:
			final Intent sendIntent = new Intent(Intent.ACTION_SEND);
			sendIntent.putExtra(Intent.EXTRA_SUBJECT,
					"Announcify - Problem using " + model.getName(info.id));
			sendIntent.putExtra(Intent.EXTRA_TEXT, "");
			sendIntent.putExtra(Intent.EXTRA_EMAIL,
					new String[] { "tom@announcify.com" });
			sendIntent.setType("message/rfc822");
			startActivity(sendIntent);

			break;
		}

		return true;
	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.actionbar_list);

		getListView().setBackgroundColor(Color.WHITE);
		getListView().setCacheColorHint(Color.TRANSPARENT);
		getListView().setFastScrollEnabled(true);

		sendStickyBroadcast(new Intent("com.announcify.ACTION_PLUGIN_CONTACT"));

		registerForContextMenu(getListView());

		getListView().setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(final AdapterView<?> arg0, final View arg1,
					final int arg2, final long arg3) {
				try {
					startActivity(new Intent(model.getAction(arg3)));
				} catch (final Exception e) {
					model.remove(arg3);

					Toast.makeText(AnnouncifyActivity.this,
							getString(R.string.toast_plugin_not_found),
							Toast.LENGTH_LONG).show();
				}
			}
		});

		model = new PluginModel(this);

		refreshList();
	}

	@Override
	public void onCreateContextMenu(final ContextMenu menu, final View v,
			final ContextMenuInfo menuInfo) {
		final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		menu.setHeaderTitle(model.getName(info.id));

		getMenuInflater().inflate(R.menu.context_main, menu);
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		super.onCreateOptionsMenu(menu);

		getMenuInflater().inflate(R.menu.menu_main, menu);

		return true;
	}

	@Override
	public boolean onMenuItemSelected(final int featureId, final MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_toggle:
			final boolean activate = !model.getActive(model
					.getId("Announcify++"));

			final Cursor cursor = model.getAll();
			cursor.moveToFirst();

			final int idIndex = cursor.getColumnIndex(BaseColumns._ID);
			do {
				model.setActive(cursor.getLong(idIndex), activate);
			} while (cursor.moveToNext());
			cursor.close();

			adapter.notifyDataSetChanged();
			break;

		case R.id.menu_help:
			startActivity(new Intent(this, HelpActivity.class));

			break;

		case R.id.menu_plugins:
			startActivity(ActivityUtils.getPluginsIntent());

			break;

		case R.id.menu_rate:
			startActivity(new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://market.announcify.com/")));

			break;

		case R.id.menu_share:
			startActivity(ActivityUtils.getShareIntent(this));

			break;

		case R.id.menu_translate:
			startActivity(new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://goo.gl/MmR5D")));

			break;
		}

		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	protected void onRestart() {
		super.onRestart();

		sendStickyBroadcast(new Intent("com.announcify.ACTION_PLUGIN_CONTACT"));
		refreshList();
	}

	@Override
	protected void onStop() {
		getContentResolver().unregisterContentObserver(observer);
		cursor.close();

		super.onStop();
	}

	private void refreshList() {
		try {
			createPackageContext("org.mailboxer.saymyname.donate", 0);

			if (model.getId("BbAdMob") >= 0) {
				model.remove(model.getId("BbAdMob"));
			}
		} catch (final Exception e) {
			// TODO: SectionedAdapter doesn't support lower-case.
			if (model.getId("BbAdMob") < 0) {
				model.add("BbAdMob", 9, "", "com.google.ad", false);
			}
		}

		cursor = model.getAll(PluginModel.KEY_PLUGIN_NAME);

		observer = new AnnouncifyObserver(new Handler());
		getContentResolver().registerContentObserver(
				Uri.withAppendedPath(AnnouncifyProvider.PROVIDER_URI,
						PluginModel.TABLE_NAME), false, observer);

		adapter = new SectionedAdapter(this, model, cursor);
		getListView().setAdapter(adapter);
	}
}
