package com.svenjacobs.androiddbpoc.service;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import com.svenjacobs.androiddbpoc.common.SampleContract;

/**
 * @author Sven Jacobs
 */
public class SampleContentProvider extends ContentProvider {
    private static final int PEOPLE = 1;
    private static final int PEOPLE_ID = 2;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private SQLiteOpenHelper openHelper;

    static {
        uriMatcher.addURI("com.svenjacobs.androiddbpoc", "people", PEOPLE);
        uriMatcher.addURI("com.svenjacobs.androiddbpoc", "people/#", PEOPLE_ID);
    }

    @Override
    public boolean onCreate() {
        openHelper = new SampleDataOpenHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(final Uri uri,
                        final String[] projection,
                        final String selection,
                        final String[] selectionArgs,
                        final String sortOrder) {

        try {
            final SQLiteDatabase db = openHelper.getReadableDatabase();

            final SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

            queryBuilder.setTables(SampleContract.PEOPLE_TABLE);

            if (uriMatcher.match(uri) == PEOPLE_ID) {
                queryBuilder.appendWhere(BaseColumns._ID + "=" + ContentUris.parseId(uri));
            }

            final Cursor c = queryBuilder.query(
                    db,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder);

            c.setNotificationUri(getContext().getContentResolver(), uri);

            return c;
        } catch (SQLiteException e) {
            return null;
        }
    }

    @Override
    public Uri insert(final Uri uri,
                      final ContentValues values) {

        final SQLiteDatabase db = openHelper.getWritableDatabase();

        final long id = db.insert(SampleContract.PEOPLE_TABLE, null, values);

        getContext().getContentResolver().notifyChange(SampleContract.PEOPLE_URI, null);

        return ContentUris.withAppendedId(SampleContract.PEOPLE_URI, id);
    }

//    @Override
//    public int bulkInsert(final Uri uri,
//                          final ContentValues[] values) {
//
//        final SQLiteDatabase db = openHelper.getWritableDatabase();
//        final DatabaseUtils.InsertHelper helper = new DatabaseUtils.InsertHelper(db, SampleContract.PEOPLE_TABLE);
//
//        for (final ContentValues value : values) {
//            helper.replace(value);
//        }
//
//        return values.length;
//    }

    @Override
    public int delete(final Uri uri,
                      final String selection,
                      final String[] selectionArgs) {

        final SQLiteDatabase db = openHelper.getWritableDatabase();

        getContext().getContentResolver().notifyChange(SampleContract.PEOPLE_URI, null);

        return db.delete(SampleContract.PEOPLE_TABLE, selection, selectionArgs);
    }

    @Override
    public int update(final Uri uri,
                      final ContentValues values,
                      final String selection,
                      final String[] selectionArgs) {

        final SQLiteDatabase db = openHelper.getWritableDatabase();

        final long id = ContentUris.parseId(uri);

        getContext().getContentResolver().notifyChange(ContentUris.withAppendedId(SampleContract.PEOPLE_URI, id), null);

        return db.update(SampleContract.PEOPLE_TABLE, values, selection, selectionArgs);
    }

    @Override
    public String getType(final Uri uri) {
        switch (uriMatcher.match(uri)) {
            case PEOPLE:
                return "vnd.android.cursor.dir/person";
            case PEOPLE_ID:
                return "vnd.android.cursor.item/person";
            default:
                return null;
        }
    }
}
