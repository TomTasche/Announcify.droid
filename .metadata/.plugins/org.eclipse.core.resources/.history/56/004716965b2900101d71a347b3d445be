
package com.announcify.ui.widget;

import greendroid.widget.ItemAdapter;
import greendroid.widget.item.Item;

import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AlphabetIndexer;
import android.widget.ListAdapter;
import android.widget.SectionIndexer;

public class SectionedItemAdapter extends ItemAdapter implements SectionIndexer {
    private static final String SECTIONS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private boolean byPriority;

    private final AlphabetIndexer indexer;

    public SectionedItemAdapter(final Context context, final List<Item> items) {
        super(context, items);

        indexer = new AlphabetIndexer(new FakeCursor(this), 0, SECTIONS);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();

        indexer.onChanged();
    }

    public void toggleSort() {
        byPriority = !byPriority;

        notifyDataSetChanged();
    }

    public int getPositionForSection(final int sectionIndex) {
        return indexer.getPositionForSection(sectionIndex);
    }

    public int getSectionForPosition(final int position) {
        return indexer.getSectionForPosition(position);
    }

    public Object[] getSections() {
        return indexer.getSections();
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        final PluginItem item = (PluginItem)getItem(position);
        final int section = getSectionForPosition(position);

        if (getPositionForSection(section) == position) {
            item.header = indexer.getSections()[section].toString().trim();
        } else {
            item.header = null;
        }

        return super.getView(position, convertView, parent);
    }

    /**
     * An implementation of a Cursor that is almost useless. It is simply used
     * for the SectionIndexer to browse our underlying data.
     * 
     * @author Cyril Mottier
     */
    private class FakeCursor implements Cursor {

        private final ListAdapter mAdapter;

        private int mPosition;

        public FakeCursor(final ListAdapter adapter) {
            mAdapter = adapter;
        }

        public void close() {
        }

        public void copyStringToBuffer(final int columnIndex, final CharArrayBuffer buffer) {
        }

        public void deactivate() {
        }

        public byte[] getBlob(final int columnIndex) {
            return null;
        }

        public int getColumnCount() {
            return 0;
        }

        public int getColumnIndex(final String columnName) {
            return 0;
        }

        public int getColumnIndexOrThrow(final String columnName) throws IllegalArgumentException {
            return 0;
        }

        public String getColumnName(final int columnIndex) {
            return null;
        }

        public String[] getColumnNames() {
            return null;
        }

        public int getCount() {
            return mAdapter.getCount();
        }

        public double getDouble(final int columnIndex) {
            return 0;
        }

        public Bundle getExtras() {
            return null;
        }

        public float getFloat(final int columnIndex) {
            return 0;
        }

        public int getInt(final int columnIndex) {
            return 0;
        }

        public long getLong(final int columnIndex) {
            return 0;
        }

        public int getPosition() {
            return 0;
        }

        public short getShort(final int columnIndex) {
            return 0;
        }

        public String getString(final int columnIndex) {
            final PluginItem item = (PluginItem)mAdapter.getItem(mPosition);

            if (byPriority) {
                final int priority = item.getPriority();

                if (priority == 1) {
                    return priority + " - highest priority";
                } else if (priority == 9) {
                    return priority + " - lowest priority";
                }

                return String.valueOf(priority);
            } else {
                if ("".equals(item.getName())) {
                    return "";
                }

                return item.getName().substring(0, 1);
            }
        }

        public boolean getWantsAllOnMoveCalls() {
            return false;
        }

        public boolean isAfterLast() {
            return false;
        }

        public boolean isBeforeFirst() {
            return false;
        }

        public boolean isClosed() {
            return false;
        }

        public boolean isFirst() {
            return false;
        }

        public boolean isLast() {
            return false;
        }

        public boolean isNull(final int columnIndex) {
            return false;
        }

        public boolean move(final int offset) {
            return false;
        }

        public boolean moveToFirst() {
            return false;
        }

        public boolean moveToLast() {
            return false;
        }

        public boolean moveToNext() {
            return false;
        }

        public boolean moveToPosition(final int position) {
            if (position < -1 || position > getCount()) {
                return false;
            }
            mPosition = position;
            return true;
        }

        public boolean moveToPrevious() {
            return false;
        }

        public void registerContentObserver(final ContentObserver observer) {
        }

        public void registerDataSetObserver(final DataSetObserver observer) {
        }

        public boolean requery() {
            return false;
        }

        public Bundle respond(final Bundle extras) {
            return null;
        }

        public void setNotificationUri(final ContentResolver cr, final Uri uri) {
        }

        public void unregisterContentObserver(final ContentObserver observer) {
        }

        public void unregisterDataSetObserver(final DataSetObserver observer) {
        }
    }
}
