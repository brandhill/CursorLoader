package com.svenjacobs.androiddbpoc.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.RandomStringUtils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;

import com.svenjacobs.androiddbpoc.common.SampleContract;

/**
 * @author Sven Jacobs
 */
public class RandomDataGenerator {

    private final Context context;
    private final ScheduledExecutorService executor;
    private final Random random;

    public RandomDataGenerator(final Context context) {
        this.context = context;
        this.executor = Executors.newSingleThreadScheduledExecutor();
        this.random = new Random();
    }

    public void start() {
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                 switch(random.nextInt(2)) {
                     case 0:
                         insert();
                         break;
                     case 1:
                         delete();
                         break;
                 }
            }
        }, 2, 2, TimeUnit.SECONDS);
    }

    public void stop() {
        executor.shutdownNow();
    }

    public void insertMultiple(int count) {
        for (int i = 0; i < count; i++) {
            insert();
        }
    }

    private void insert() {
        final ContentValues values = new ContentValues();

        values.put(SampleContract.Data.FIRST_NAME, RandomStringUtils.randomAlphabetic(10));
        values.put(SampleContract.Data.LAST_NAME, RandomStringUtils.randomAlphabetic(10));

        context.getContentResolver().insert(SampleContract.PEOPLE_URI, values);
    }

    private void delete() {
        final ContentResolver resolver = context.getContentResolver();
        final Cursor c = resolver.query(SampleContract.PEOPLE_URI, null, null, null, null);
        final List<Integer> ids = new ArrayList<Integer>();

        try {
            while (c.moveToNext()) {
                final int id = c.getInt(c.getColumnIndex(BaseColumns._ID));
                ids.add(id);
            }
        } finally {
            c.close();
        }

        if (ids.size() > 0) {
            final int id = ids.get(random.nextInt(ids.size()));
            resolver.delete(SampleContract.PEOPLE_URI, BaseColumns._ID + "=?", new String[] {Integer.toString(id)});
        }
    }
}
