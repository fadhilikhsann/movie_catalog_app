package com.example.fadhilikhsann.favouritemovieapp.helper;

import android.database.Cursor;

import com.example.fadhilikhsann.favouritemovieapp.Movie.MovieItems;
import com.example.fadhilikhsann.favouritemovieapp.db.DatabaseContract_Movie;

import java.util.ArrayList;

public class MappingHelper {
    public static ArrayList<MovieItems> mapCursorToArrayListMovie(Cursor movieCursor) {
        ArrayList<MovieItems> movieList = new ArrayList<>();
        while (movieCursor.moveToNext()) {
            int id = movieCursor.getInt(movieCursor.getColumnIndexOrThrow(DatabaseContract_Movie.MovieColumns._ID));
            String title = movieCursor.getString(movieCursor.getColumnIndexOrThrow(DatabaseContract_Movie.MovieColumns.TITLE));
            String poster = movieCursor.getString(movieCursor.getColumnIndexOrThrow(DatabaseContract_Movie.MovieColumns.POSTER));
            String id_movie = movieCursor.getString(movieCursor.getColumnIndexOrThrow(DatabaseContract_Movie.MovieColumns.ID_MOVIE));
            movieList.add(new MovieItems(id_movie, title, poster));
        }
        return movieList;
    }

    public static MovieItems mapCursorToObject(Cursor movieCursor) {
        movieCursor.moveToFirst();
        int id = movieCursor.getInt(movieCursor.getColumnIndexOrThrow(DatabaseContract_Movie.MovieColumns._ID));
        String title = movieCursor.getString(movieCursor.getColumnIndexOrThrow(DatabaseContract_Movie.MovieColumns.TITLE));
        String poster = movieCursor.getString(movieCursor.getColumnIndexOrThrow(DatabaseContract_Movie.MovieColumns.POSTER));
        String id_movie = movieCursor.getString(movieCursor.getColumnIndexOrThrow(DatabaseContract_Movie.MovieColumns.ID_MOVIE));
        return new MovieItems(id_movie, title, poster);
    }
}
