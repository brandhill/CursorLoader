package com.svenjacobs.androiddbpoc.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import com.svenjacobs.androiddbpoc.common.SampleContract;

/**
 * @author Sven Jacobs
 */
public class SampleDataOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "sample";

    public SampleDataOpenHelper(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + SampleContract.PEOPLE_TABLE
                + " (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SampleContract.Data.FIRST_NAME + " TEXT, "
                + SampleContract.Data.LAST_NAME + " TEXT)");
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
    }
}
