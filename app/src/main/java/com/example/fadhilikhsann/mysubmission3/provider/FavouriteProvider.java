package com.example.fadhilikhsann.mysubmission3.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.example.fadhilikhsann.mysubmission3.db.MovieHelper;

import java.sql.SQLException;

import static com.example.fadhilikhsann.mysubmission3.db.DatabaseContract_Movie.AUTHORITY;
import static com.example.fadhilikhsann.mysubmission3.db.DatabaseContract_Movie.CONTENT_URI;
import static com.example.fadhilikhsann.mysubmission3.db.DatabaseContract_Movie.TABLE_NAME;

public class FavouriteProvider extends ContentProvider {

    private static final int MOVIE = 1;
    private static final int MOVIE_ID = 2;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        sUriMatcher.addURI(AUTHORITY, TABLE_NAME, MOVIE);

        sUriMatcher.addURI(AUTHORITY,
                TABLE_NAME + "/#",
                MOVIE_ID);
    }

    private MovieHelper movieHelper;


    public FavouriteProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        int deleted;
        switch (sUriMatcher.match(uri)) {
            case MOVIE_ID:
                deleted = movieHelper.deleteById(uri.getLastPathSegment());
                break;
            default:
                deleted = 0;
                break;
        }
        getContext().getContentResolver().notifyChange(CONTENT_URI, null);
        return deleted;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        long added;
        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                added = movieHelper.insert(values);
                break;
            default:
                added = 0;
                break;
        }
        getContext().getContentResolver().notifyChange(CONTENT_URI, null);
        return Uri.parse(CONTENT_URI + "/" + added);
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        movieHelper = MovieHelper.getInstance(getContext());
        try {
            movieHelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        Cursor cursor;

        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                cursor = movieHelper.queryAll();
                break;
            case MOVIE_ID:
                cursor = movieHelper.queryById(uri.getLastPathSegment());
                Log.d("PROVIDER", "query: " + cursor.getCount());
                break;
            default:
                cursor = null;
                break;
        }

        return cursor;

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        int updated;
        switch (sUriMatcher.match(uri)) {
            case MOVIE_ID:
                updated = movieHelper.update(uri.getLastPathSegment(), values);
                break;
            default:
                updated = 0;
                break;
        }
        getContext().getContentResolver().notifyChange(CONTENT_URI, null);
        return updated;
    }
}
