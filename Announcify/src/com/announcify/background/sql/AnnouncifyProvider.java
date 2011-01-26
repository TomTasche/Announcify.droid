package com.announcify.background.sql;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class AnnouncifyProvider extends ContentProvider {

    private AnnouncifyDatabase announcify;


    @Override
    public boolean onCreate() {
        announcify = new AnnouncifyDatabase(getContext());

        return false;
    }

    @Override
    public int delete(Uri arg0, String arg1, String[] arg2) {
        SQLiteDatabase database = announcify.getWritableDatabase();
        database.delete(arg0.getLastPathSegment(), arg1, arg2);
        database.close();

        return 0;
    }

    @Override
    public String getType(Uri uri) {
        return "announcify";
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase database = announcify.getWritableDatabase();
        database.insert(uri.getLastPathSegment(), null, values);
        database.close();

        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        SQLiteDatabase database = announcify.getReadableDatabase();

        try {
            return database.query(uri.getLastPathSegment(), projection, selection, selectionArgs, null, null, sortOrder);
        } finally {
            database.close();
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase database = announcify.getWritableDatabase();
        database.update(uri.getLastPathSegment(), values, selection, selectionArgs);
        database.close();

        return 0;
    }
}
