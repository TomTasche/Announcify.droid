package com.announcify.ui.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.Groups;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.announcify.R;
import com.announcify.api.background.sql.model.GroupModel;

public class GroupActivity extends BaseActivity {
    
    private ListView list;
    
    private SimpleCursorAdapter adapter;
    
    private GroupModel model;
    
    private Cursor cursor;
    
    
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        
        setActionBarContentView(R.layout.list);
        
        adapter = new SimpleCursorAdapter(this, R.layout.list_item_choose, null, new String[] {Groups._ID}, new int[] {R.id.textLeft});
        
        list = (ListView)findViewById(android.R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
               
            }
        });
        
        model = new GroupModel(this);
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        
        cursor = getContentResolver().query(Groups.CONTENT_URI, new String[] {Groups._ID, Groups.TITLE}, null, null, Groups.TITLE);
    }
}
