
package com.announcify.ui.activity;

import greendroid.widget.item.Item;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
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

import com.announcify.R;
import com.announcify.api.background.receiver.PluginReceiver;
import com.announcify.api.background.sql.model.PluginModel;
import com.announcify.ui.widget.PluginItem;
import com.announcify.ui.widget.PluginItemView;
import com.announcify.ui.widget.SectionedItemAdapter;

public class AnnouncifyActivity extends BaseActivity {

    private PluginModel model;

    private PluginExplorer explorer;

    private SectionedItemAdapter adapter;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setActionBarContentView(R.layout.list);

        getListView().setBackgroundColor(Color.WHITE);
        getListView().setCacheColorHint(Color.TRANSPARENT);
        getListView().setFastScrollEnabled(true);

        registerForContextMenu(getListView());

        getListView().setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(final AdapterView<?> arg0, final View arg1, final int arg2,
                    final long arg3) {
                if (!((PluginItem)getListView().getItemAtPosition(arg2)).fireAction()) {
                    Toast.makeText(AnnouncifyActivity.this,
                            "The Plugin you are looking for seems to be uninstalled!",
                            Toast.LENGTH_LONG).show();

                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        model = new PluginModel(this);

        explorer = new PluginExplorer();
        registerReceiver(explorer, new IntentFilter(PluginReceiver.ACTION_PLUGIN_RESPOND));
        sendBroadcast(new Intent(PluginReceiver.ACTION_PLUGIN_CONTACT),
                PluginReceiver.PERMISSION_IM_A_PLUGIN);

        final List<Item> items = new ArrayList<Item>();
        final Cursor cursor = model.getAll();

        while (cursor.moveToNext()) {
            items.add(new PluginItem(this, model, cursor.getInt(cursor.getColumnIndex(BaseColumns._ID))));
        }

        cursor.close();

        adapter = new SectionedItemAdapter(this, items);
        getListView().setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        // TODO: if we didn't receive every plugin that's in the db: increase
        // rip timer!
        // or register for package installed broadcasts and check packages

        unregisterReceiver(explorer);

        super.onPause();
    }

    private ListView getListView() {
        return (ListView)findViewById(android.R.id.list);
    }

    @Override
    public void onCreateContextMenu(final ContextMenu menu, final View v,
            final ContextMenuInfo menuInfo) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        final PluginItem item = (PluginItem)adapter.getItem(info.position);
        menu.setHeaderTitle(item.getName());

        getMenuInflater().inflate(R.menu.context_main, menu);
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item
        .getMenuInfo();

        final long id = model.getId(((PluginItemView)info.targetView).getText());

        switch (item.getItemId()) {
            case R.id.menu_uninstall:
                startActivity(new Intent(Intent.ACTION_DELETE, Uri.parse("package:"
                        + model.getPackage(id))));

                break;

            case R.id.menu_activate:
                model.togglePlugin(id);

                adapter.notifyDataSetChanged();

                break;

            case R.id.menu_report:
                final Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_SUBJECT,
                        "Announcify - Problem with " + model.getName(id));
                sendIntent.putExtra(Intent.EXTRA_TEXT, "");
                sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {
                        "tom@announcify.com"
                });
                sendIntent.setType("message/rfc822");
                startActivity(sendIntent);

                break;
        }

        return true;
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
                final ContentValues values = new ContentValues();
                values.put(PluginModel.KEY_PLUGIN_ACTIVE,
                        !model.getActive(model.getId("Announcify++")));
                model.getResolver().update(model.buildUri(), values, null, null);

                adapter.notifyDataSetChanged();

                break;

            case R.id.menu_help:
                startActivity(new Intent(this, HelpActivity.class));

                break;

            case R.id.menu_plugins:
                morePlugins();

                break;

            case R.id.menu_rate:
                // TODO: check URL
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.appbrain.com/app/announcify/com.announcify?install")));

                break;

            case R.id.menu_share:
                spreadTheWord();

                break;

            case R.id.menu_about:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://announcify.com/")));

                break;
        }

        return super.onMenuItemSelected(featureId, item);
    }

    class PluginExplorer extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            final long id = model.getId(intent.getStringExtra(PluginReceiver.EXTRA_PLUGIN_NAME));

            if (id < 0) {
                model.add(intent.getStringExtra(PluginReceiver.EXTRA_PLUGIN_NAME),
                        intent.getIntExtra(PluginReceiver.EXTRA_PLUGIN_PRIORITY, 10),
                        intent.getStringExtra(PluginReceiver.EXTRA_PLUGIN_ACTION),
                        intent.getStringExtra(PluginReceiver.EXTRA_PLUGIN_PACKAGE),
                        intent.getBooleanExtra(PluginReceiver.EXTRA_PLUGIN_BROADCAST, false));

                adapter.add(new PluginItem(AnnouncifyActivity.this, model, model.getId(intent
                        .getStringExtra(PluginReceiver.EXTRA_PLUGIN_NAME))));
                adapter.notifyDataSetChanged();
            }
        }
    }
}
