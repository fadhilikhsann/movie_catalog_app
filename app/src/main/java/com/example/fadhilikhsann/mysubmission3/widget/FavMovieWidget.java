package com.example.fadhilikhsann.mysubmission3.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.example.fadhilikhsann.mysubmission3.Movie.MovieItems;
import com.example.fadhilikhsann.mysubmission3.R;
import com.example.fadhilikhsann.mysubmission3.db.MovieHelper;
import com.example.fadhilikhsann.mysubmission3.favourite.FavouriteMovieActivity;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class FavMovieWidget extends AppWidgetProvider {

    public static final String EXTRA_ITEM = "EXTRA_ITEM";
    private static final String TOAST_ACTION = "TOAST_ACTION";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {


        CharSequence widgetText = context.getString(R.string.favMovie);
        // Construct the RemoteViews object
        //RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.fav_movie_widget);
        //views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget


        Intent intent = new Intent(context, StackWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.fav_movie_widget);
        views.setRemoteAdapter(R.id.stack_view, intent);


        Intent toastIntent = new Intent(context, FavMovieWidget.class);
        toastIntent.setAction(FavMovieWidget.TOAST_ACTION);
        toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction() != null) {
            if (intent.getAction().equals(TOAST_ACTION)) {
                int viewIndex = intent.getIntExtra(EXTRA_ITEM, 0);
                MovieHelper movieHelper = new MovieHelper(context);
                try {
                    movieHelper.open();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                ArrayList<MovieItems> listFavMovie = movieHelper.getAllData();
                //movieHelper.close();
                //MovieItems movieItems = listFavMovie.get(viewIndex);
                Intent moveWithObjectIntent = new Intent(context, FavouriteMovieActivity.class);
                //moveWithObjectIntent.putExtra(DetailMovieActivity.EXTRA_ID, String.valueOf(movieItems.getId()));
                moveWithObjectIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(moveWithObjectIntent);
            }
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

