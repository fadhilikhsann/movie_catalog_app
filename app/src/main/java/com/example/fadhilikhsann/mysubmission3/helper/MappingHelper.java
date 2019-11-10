package com.example.fadhilikhsann.mysubmission3.helper;

import android.database.Cursor;

import com.example.fadhilikhsann.mysubmission3.Movie.MovieItems;
import com.example.fadhilikhsann.mysubmission3.TvShow.TvShowItems;
import com.example.fadhilikhsann.mysubmission3.db.DatabaseContract_Movie;
import com.example.fadhilikhsann.mysubmission3.db.DatabaseContract_TvShow;

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

    public static ArrayList<TvShowItems> mapCursorToArrayListTvShow(Cursor tvShowCursor) {
        ArrayList<TvShowItems> tvShowList = new ArrayList<>();
        while (tvShowCursor.moveToNext()) {
            int id = tvShowCursor.getInt(tvShowCursor.getColumnIndexOrThrow(DatabaseContract_TvShow.TvShowColumns._ID));
            String title = tvShowCursor.getString(tvShowCursor.getColumnIndexOrThrow(DatabaseContract_TvShow.TvShowColumns.TITLE));
            String poster = tvShowCursor.getString(tvShowCursor.getColumnIndexOrThrow(DatabaseContract_TvShow.TvShowColumns.POSTER));
            String id_tv_show = tvShowCursor.getString(tvShowCursor.getColumnIndexOrThrow(DatabaseContract_TvShow.TvShowColumns.ID_TV_SHOW));
            tvShowList.add(new TvShowItems(id_tv_show, title, poster));
        }
        return tvShowList;
    }
}
