package com.example.fadhilikhsann.mysubmission3.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.fadhilikhsann.mysubmission3.Movie.MovieItems;

import java.sql.SQLException;
import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.example.fadhilikhsann.mysubmission3.db.DatabaseContract_Movie.MovieColumns.ID_MOVIE;
import static com.example.fadhilikhsann.mysubmission3.db.DatabaseContract_Movie.MovieColumns.POSTER;
import static com.example.fadhilikhsann.mysubmission3.db.DatabaseContract_Movie.MovieColumns.TITLE;
import static com.example.fadhilikhsann.mysubmission3.db.DatabaseContract_Movie.TABLE_NAME;

public class MovieHelper {
    private static final String DATABASE_TABLE_MOVIE = TABLE_NAME;
    private static DatabaseHelper dataBaseHelper;
    private static MovieHelper INSTANCE;
    private static SQLiteDatabase database;

    public MovieHelper(Context context) {
        dataBaseHelper = new DatabaseHelper(context);
    }

    public static MovieHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MovieHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
        database = dataBaseHelper.getWritableDatabase();
    }

    public void close() {
        dataBaseHelper.close();
        if (database.isOpen())
            database.close();
    }

    public Cursor queryAll() {
        return database.query(
                DATABASE_TABLE_MOVIE,
                null,
                null,
                null,
                null,
                null,
                _ID + " ASC");
    }

    public Cursor queryById(String id) {
        //Log.d(TAG, "select * from "+DATABASE_TABLE_MOVIE+" where "+ID_MOVIE+"="+id+"");
        //Log.d("PROVIDER", "query: "+database.rawQuery( "select * from "+DATABASE_TABLE_MOVIE+" where "+ID_MOVIE+"="+id+"", null ).getCount());
        return database.rawQuery("select * from " + DATABASE_TABLE_MOVIE + " where " + ID_MOVIE + "=" + id + "", null);
    }

    public ArrayList<MovieItems> getAllData() {
        Log.d("MVIE HELPER DATABASE:", "status : create");
        Cursor cursor = database.query(
                DATABASE_TABLE_MOVIE,
                null,
                null,
                null,
                null,
                null,
                _ID + " ASC");
        cursor.moveToFirst();
        ArrayList<MovieItems> arrayList = new ArrayList<>();
        MovieItems movieItems;
        if (cursor.getCount() > 0) {
            do {
                movieItems = new MovieItems(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(_ID))), cursor.getString(cursor.getColumnIndexOrThrow(TITLE)), cursor.getString(cursor.getColumnIndexOrThrow(POSTER)));
                arrayList.add(movieItems);
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long insert(ContentValues values) {
        return database.insert(DATABASE_TABLE_MOVIE, null, values);
    }

    public int update(String id, ContentValues values) {
        return database.update(DATABASE_TABLE_MOVIE, values, _ID + " = ?", new String[]{id});
    }

    public int deleteById(String id) {
        return database.delete(DATABASE_TABLE_MOVIE, ID_MOVIE + " = ?", new String[]{id});
    }
}
