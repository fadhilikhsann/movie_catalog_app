package com.example.fadhilikhsann.mysubmission3.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;

import static android.provider.BaseColumns._ID;
import static com.example.fadhilikhsann.mysubmission3.db.DatabaseContract_TvShow.TABLE_NAME;
import static com.example.fadhilikhsann.mysubmission3.db.DatabaseContract_TvShow.TvShowColumns.ID_TV_SHOW;

public class TvShowHelper {
    private static final String DATABASE_TABLE_TV_SHOW = TABLE_NAME;
    private static DatabaseHelper dataBaseHelper;
    private static TvShowHelper INSTANCE;
    private static SQLiteDatabase database;

    private TvShowHelper(Context context) {
        dataBaseHelper = new DatabaseHelper(context);
    }

    public static TvShowHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TvShowHelper(context);
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
                DATABASE_TABLE_TV_SHOW,
                null,
                null,
                null,
                null,
                null,
                _ID + " ASC");
    }

    public Cursor queryById(String id) {
        //Log.d(TAG, "select * from "+DATABASE_TABLE_TV_SHOW+" where "+ID_TV_SHOW+"="+id+"");
        return database.rawQuery("select * from " + DATABASE_TABLE_TV_SHOW + " where " + ID_TV_SHOW + "=" + id + "", null);
    }

    public long insert(ContentValues values) {
        return database.insert(DATABASE_TABLE_TV_SHOW, null, values);
    }

    public int update(String id, ContentValues values) {
        return database.update(DATABASE_TABLE_TV_SHOW, values, _ID + " = ?", new String[]{id});
    }

    public int deleteById(String id) {
        return database.delete(DATABASE_TABLE_TV_SHOW, ID_TV_SHOW + " = ?", new String[]{id});
    }
}
