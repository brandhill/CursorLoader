package com.svenjacobs.androiddbpoc.common;

import android.net.Uri;

/**
 * @author Sven Jacobs
 */
public final class SampleContract {
    private SampleContract() {
    }

    public static interface Data {
        public static final String FIRST_NAME = "first_name";
        public static final String LAST_NAME = "last_name";
    }

    public static final String PEOPLE_TABLE = "people";

    public static final Uri AUTHORITY = Uri.parse("content://com.svenjacobs.androiddbpoc.sample");
    public static final Uri PEOPLE_URI = Uri.withAppendedPath(AUTHORITY, PEOPLE_TABLE);
}
