package com.svenjacobs.androiddbpoc.client;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.View;
import android.widget.SimpleCursorAdapter;

import com.svenjacobs.androiddbpoc.R;
import com.svenjacobs.androiddbpoc.common.SampleContract;
import com.svenjacobs.androiddbpoc.service.SampleDataOpenHelper;

public class MainActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    String TAG = "MainActivity";
    
    private SimpleCursorAdapter adapter;
    private RandomDataGenerator generator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);

        final String[] fromColumns = {SampleContract.Data.FIRST_NAME, SampleContract.Data.LAST_NAME};
        final int[] toViews = {android.R.id.text1, android.R.id.text2};

        adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, null, fromColumns, toViews, 0);
        setListAdapter(adapter);

        getLoaderManager().initLoader(0, null, this);

        generator = new RandomDataGenerator(this);
        generator.insertMultiple(20);
        generator.start();
    }

    @Override
    public Loader<Cursor> onCreateLoader(final int id,
                                         final Bundle args) {
        return new CursorLoader(this, SampleContract.PEOPLE_URI,
                new String[] {BaseColumns._ID, SampleContract.Data.FIRST_NAME, SampleContract.Data.LAST_NAME},
                null, null, null);
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader,
                               final Cursor data) {
        
        Log.d(TAG, "onLoadFinished");
        
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    public void onDeleteClick(final View view) {
        final SampleDataOpenHelper helper = new SampleDataOpenHelper(this);
        helper.getWritableDatabase().execSQL("DELETE FROM " + SampleContract.PEOPLE_TABLE);
    }
}
