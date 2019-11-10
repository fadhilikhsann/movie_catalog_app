package com.example.fadhilikhsann.favouritemovieapp.db;

import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract_Movie {
    public static final String AUTHORITY = "com.example.fadhilikhsann.mysubmission3";
    private static final String SCHEME = "content";
    public static String TABLE_NAME = "favourite_movie";
    public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath(TABLE_NAME)
            .build();

    public static final class MovieColumns implements BaseColumns {
        public static String ID_MOVIE = "id_movie";
        public static String POSTER = "poster";
        public static String TITLE = "title";
    }
}
