package com.announcify.ui.activity;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.announcify.R;
import com.announcify.api.background.sql.model.TranslationModel;
import com.announcify.api.ui.activity.BaseActivity;
import com.announcify.background.tts.Speaker;

public class ReplaceActivity extends BaseActivity {

    private SimpleCursorAdapter adapter;
    private EditText with;
    private EditText replace;
    private ListView list;
    private TranslationModel model;
    private Cursor cursor;
    private Speaker speaker;

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.menu_edit:

                final Cursor text = model.get(info.id);
                text.moveToFirst();

                replace.setText(text.getString(text.getColumnIndex(TranslationModel.KEY_TRANSLATION_FROM)));
                with.setText(text.getString(text.getColumnIndex(TranslationModel.KEY_TRANSLATION_TO)));

                text.close();

            case R.id.menu_remove:

                model.remove(info.id);

                break;
        }

        cursor.close();
        cursor = model.getAll(TranslationModel.KEY_TRANSLATION_FROM);

        adapter.changeCursor(cursor);

        return super.onContextItemSelected(item);
    }

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle, R.layout.activity_replace);

        with = (EditText) findViewById(R.id.edit_with);
        replace = (EditText) findViewById(R.id.edit_replace);
        list = (ListView) findViewById(android.R.id.list);

        adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, null, new String[] { TranslationModel.KEY_TRANSLATION_FROM, TranslationModel.KEY_TRANSLATION_TO }, new int[] { android.R.id.text1, android.R.id.text2 });

        list.setBackgroundColor(Color.WHITE);
        list.setCacheColorHint(Color.TRANSPARENT);
        list.setFastScrollEnabled(true);
        list.setAdapter(adapter);
        registerForContextMenu(list);

        findViewById(R.id.button_speak).setOnClickListener(new OnClickListener() {

            public void onClick(final View v) {
                speaker.speak(with.getText().toString());
            }
        });

        findViewById(R.id.button_add).setOnClickListener(new OnClickListener() {

            public void onClick(final View v) {
                model.add(replace.getText().toString(), with.getText().toString(), "", "");

                cursor.close();
                cursor = model.getAll(TranslationModel.KEY_TRANSLATION_FROM);

                adapter.changeCursor(cursor);
            }
        });
    }

    @Override
    public void onCreateContextMenu(final ContextMenu menu, final View v, final ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.context_replace, menu);
    }

    @Override
    protected void onPause() {
        cursor.close();

        speaker.shutdown();

        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();

        model = new TranslationModel(this);
        cursor = model.getAll(TranslationModel.KEY_TRANSLATION_FROM);

        adapter.changeCursor(cursor);

        speaker = new Speaker(this, null);
    }
}
