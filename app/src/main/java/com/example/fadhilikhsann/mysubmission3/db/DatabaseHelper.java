package com.example.fadhilikhsann.mysubmission3.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_TABLE_MOVIE = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
            DatabaseContract_Movie.TABLE_NAME,
            DatabaseContract_Movie.MovieColumns._ID,
            DatabaseContract_Movie.MovieColumns.ID_MOVIE,
            DatabaseContract_Movie.MovieColumns.POSTER,
            DatabaseContract_Movie.MovieColumns.TITLE
    );
    private static final String SQL_CREATE_TABLE_TV_SHOW = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
            DatabaseContract_TvShow.TABLE_NAME,
            DatabaseContract_TvShow.TvShowColumns._ID,
            DatabaseContract_TvShow.TvShowColumns.ID_TV_SHOW,
            DatabaseContract_TvShow.TvShowColumns.POSTER,
            DatabaseContract_TvShow.TvShowColumns.TITLE
    );
    public static String DATABASE_NAME = "dbentertainments";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_MOVIE);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_TV_SHOW);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DatabaseContract_Movie.TABLE_NAME);
        onCreate(sqLiteDatabase);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DatabaseContract_TvShow.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}

