package com.example.fadhilikhsann.mysubmission3.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.example.fadhilikhsann.mysubmission3.Movie.MovieItems;
import com.example.fadhilikhsann.mysubmission3.R;
import com.example.fadhilikhsann.mysubmission3.db.MovieHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private final List<Bitmap> mWidgetItems = new ArrayList<>();
    private final List<String> mWidgetText = new ArrayList<>();
    private final Context mContext;
    MovieItems movieItems;
    private MovieHelper movieHelper;
    private ArrayList<MovieItems> listMovieFav = new ArrayList<>();

    StackRemoteViewsFactory(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        mWidgetItems.clear();
        mWidgetText.clear();
        movieHelper = MovieHelper.getInstance(mContext);
        try {
            movieHelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        listMovieFav = movieHelper.getAllData();

        if (listMovieFav != null && listMovieFav.size() >= 0) {
            for (int i = 0; i < listMovieFav.size(); i++) {
                movieItems = listMovieFav.get(i);
                String poster = movieItems.getPoster();
                String title = movieItems.getTitle();
                Bitmap bmp = null;
                try {
                    bmp = Glide.with(mContext)
                            .asBitmap()
                            .load("https://image.tmdb.org/t/p/original/" + poster)
                            .submit()
                            .get();
                } catch (InterruptedException | ExecutionException e) {
                    Log.d("Widget Load Image", "Error");
                }

                mWidgetItems.add(Bitmap.createBitmap(bmp));
                mWidgetText.add(title);
            }
        } else {
            mWidgetItems.clear();
            mWidgetText.clear();
        }


    }

    @Override
    public void onDestroy() {
        movieHelper.close();
    }

    @Override
    public int getCount() {
        return mWidgetItems.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        if (mWidgetItems != null && mWidgetItems.size() >= 1) {
            rv.setImageViewBitmap(R.id.imageView, mWidgetItems.get(position));
            //rv.setTextViewText(R.id.tv_movie_title,mWidgetText.get(position));
            Bundle extras = new Bundle();
            extras.putInt(FavMovieWidget.EXTRA_ITEM, position);
            Intent fillInIntent = new Intent();
            fillInIntent.putExtras(extras);
            rv.setOnClickFillInIntent(R.id.imageView, fillInIntent);
        } else {
            mWidgetItems.clear();
            mWidgetText.clear();
        }
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}