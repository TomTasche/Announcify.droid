package com.announcify.activity;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.announcify.R;
import com.announcify.activity.widget.PluginAdapter;
import com.announcify.receiver.AnnouncifyReceiver;
import com.announcify.sql.model.PluginModel;

public class Announcify extends ListActivity {
	private CheckedTextView headerName;
	private PluginAdapter adapter;
	private PluginModel model;
	private PluginExplorer pluginExplorer;
	private ProgressBar progressBar;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Window window = getWindow();
		window.requestFeature(Window.FEATURE_CUSTOM_TITLE);

		setContentView(R.layout.list_layout);

		window.setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_progress);
		progressBar = (ProgressBar) findViewById(R.id.title_progress_bar);
		progressBar.setVisibility(View.VISIBLE);

		findViewById(R.id.button_more).setOnClickListener(new OnClickListener() {

			public void onClick(final View v) {
				openOptionsMenu();
			}
		});

		pluginExplorer = new PluginExplorer();
		registerReceiver(pluginExplorer, new IntentFilter(AnnouncifyReceiver.ACTION_PLUGIN_RESPOND));
		// sendBroadcast(new Intent(AnnouncifyReceiver.ACTION_PLUGIN_CONTACT),
		// AnnouncifyReceiver.PERMISSION_IM_A_PLUGIN);
		sendBroadcast(new Intent(AnnouncifyReceiver.ACTION_PLUGIN_CONTACT));

		model = new PluginModel(this);

		addAnnouncifyHeader();

		adapter = new PluginAdapter(this, model);
		setListAdapter(adapter);
	}

	private void addAnnouncifyHeader() {
		headerName = (CheckedTextView) getLayoutInflater().inflate(android.R.layout.simple_list_item_checked, null);
		headerName.setText("Announcify");
		headerName.setChecked(model.getActive("Announcify"));
		getListView().addHeaderView(headerName);

		final TextView headerSettings = (TextView) getLayoutInflater().inflate(android.R.layout.simple_list_item_1, null);
		headerSettings.setText("Announcify Settings");
		headerSettings.setOnClickListener(new OnClickListener() {

			public void onClick(final View v) {
				Intent settingsIntent = new Intent(Announcify.this, SettingsActivity.class);
				startActivity(settingsIntent);
			}
		});
		getListView().addHeaderView(headerSettings);
	}

	@Override
	protected void onListItemClick(final ListView l, final View v, int position, final long id) {
		if (position == 0) {
			if (model.getActive("Announcify")) {
				model.togglePlugin(1);
				headerName.setChecked(false);
				setListAdapter(null);
			} else {
				model.togglePlugin(1);
				headerName.setChecked(true);
				setListAdapter(adapter);
			}
		}

		position -= 2;
		switch (adapter.getItemViewType(position)) {
		case PluginAdapter.TYPE_INTENT:
			startActivity(adapter.getIntent(position));
			break;
		case PluginAdapter.TYPE_CHECKBOX:
			model.togglePlugin(model.getId((String) adapter.getItem(position)));
			adapter.notifyDataSetChanged();
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		// TODO: do something.
		return true;
	}

	@Override
	protected void onPause() {
		pluginExplorer.unregister();

		super.onPause();
	}

	@Override
	protected void onDestroy() {
		model.close();

		super.onDestroy();
	}

	private class PluginExplorer extends BroadcastReceiver {
		private final Thread thread;

		public PluginExplorer() {
			thread = new Thread() {
				@Override
				public void run() {
					try {
						sleep(10000);
					} catch (final InterruptedException e) {} finally {
						runOnUiThread(new Runnable() {

							public void run() {
								unregisterReceiver(PluginExplorer.this);

								if (!isFinishing()) {
									progressBar.setVisibility(View.INVISIBLE);

									if (adapter.getCount() == 0) {
										// TODO: start initial setup: advertise plugins, show instructions, ...
									}
								}
							}
						});
					}
				}
			};
			thread.start();
		}

		@Override
		public void onReceive(final Context context, final Intent intent) {
			if (intent != null && intent.getExtras() != null && intent.getStringExtra(AnnouncifyReceiver.EXTRA_PLUGIN_NAME) != null && !"".equals(intent.getStringExtra(AnnouncifyReceiver.EXTRA_PLUGIN_NAME))) {
				final Intent receivedIntent = new Intent(intent.getStringExtra(AnnouncifyReceiver.EXTRA_PLUGIN_ACTION));
				adapter.add(intent.getStringExtra(AnnouncifyReceiver.EXTRA_PLUGIN_NAME), receivedIntent);
			} else {
				Log.d("Announcify", "strange intent, sorry!");
			}
		}

		public void unregister() {
			if (thread != null && thread.isAlive()) {
				thread.interrupt();
			}
		}
	}
}