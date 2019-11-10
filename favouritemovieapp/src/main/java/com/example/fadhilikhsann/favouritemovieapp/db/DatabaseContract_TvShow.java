package com.example.fadhilikhsann.favouritemovieapp.db;

import android.provider.BaseColumns;

public class DatabaseContract_TvShow {
    public static String TABLE_NAME = "favourite_tvshow";

    public static final class TvShowColumns implements BaseColumns {
        public static String ID_TV_SHOW = "id_tv_show";
        public static String POSTER = "poster";
        public static String TITLE = "title";
    }
}
