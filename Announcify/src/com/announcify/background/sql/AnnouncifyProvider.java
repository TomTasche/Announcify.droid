package com.announcify.background.sql;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class AnnouncifyProvider extends ContentProvider {

	public static final Uri PROVIDER_URI = Uri
			.parse("content://com.announcify");

	private AnnouncifyDatabase announcify;

	@Override
	public int delete(final Uri uri, final String selection,
			final String[] selectionArgs) {
		final SQLiteDatabase database = announcify.getWritableDatabase();

		final int i = database.delete(uri.getLastPathSegment(), selection,
				selectionArgs);
		if (i > 0) {
			getContext().getContentResolver().notifyChange(uri, null);
		}

		return i;
	}

	@Override
	public String getType(final Uri uri) {
		return "com.announcify";
	}

	@Override
	public Uri insert(final Uri uri, final ContentValues values) {
		final SQLiteDatabase database = announcify.getWritableDatabase();

		database.insert(uri.getLastPathSegment(), null, values);
		getContext().getContentResolver().notifyChange(uri, null);

		return null;
	}

	@Override
	public boolean onCreate() {
		announcify = new AnnouncifyDatabase(getContext());

		return true;
	}

	@Override
	public Cursor query(final Uri uri, final String[] projection,
			final String selection, final String[] selectionArgs,
			final String sortOrder) {
		final SQLiteDatabase database = announcify.getReadableDatabase();

		return database.query(uri.getLastPathSegment(), projection, selection,
				selectionArgs, null, null, sortOrder);
	}

	@Override
	public int update(final Uri uri, final ContentValues values,
			final String selection, final String[] selectionArgs) {
		final SQLiteDatabase database = announcify.getWritableDatabase();

		final int i = database.update(uri.getLastPathSegment(), values,
				selection, selectionArgs);
		if (i > 0) {
			// don't notify observers, because this would cause the UI to
			// unnecessarily refresh the whole list
			// getContext().getContentResolver().notifyChange(uri, null);
		}

		return i;
	}
}
