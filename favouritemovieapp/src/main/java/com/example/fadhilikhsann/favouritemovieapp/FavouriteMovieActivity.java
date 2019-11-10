package com.example.fadhilikhsann.favouritemovieapp;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fadhilikhsann.favouritemovieapp.Movie.DetailMovieActivity;
import com.example.fadhilikhsann.favouritemovieapp.Movie.MovieItems;
import com.example.fadhilikhsann.favouritemovieapp.adapter.MovieFavAdapter;
import com.example.fadhilikhsann.favouritemovieapp.helper.MappingHelper;
import com.google.android.material.snackbar.Snackbar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.example.fadhilikhsann.favouritemovieapp.db.DatabaseContract_Movie.CONTENT_URI;

interface LoadMovieCallback {
    void preExecute();

    void postExecute(ArrayList<MovieItems> notes);
}

public class FavouriteMovieActivity extends AppCompatActivity implements LoadMovieCallback {
    private static final String EXTRA_STATE = "EXTRA_STATE";
    public static boolean isChanged = false, edit_fav_widget = false;
    private Menu menu;
    private ProgressBar progressBar;
    private RecyclerView rvMovieFav;
    private MovieFavAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        progressBar = findViewById(R.id.progressBar);
        rvMovieFav = findViewById(R.id.recyclerView);
        rvMovieFav.setLayoutManager(new LinearLayoutManager(this));
        rvMovieFav.setHasFixedSize(true);
        adapter = new MovieFavAdapter(this);
        rvMovieFav.setAdapter(adapter);

        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        DataObserver myObserver = new DataObserver(handler, this);
        getContentResolver().registerContentObserver(CONTENT_URI, true, myObserver);


        if (savedInstanceState == null) {
            // proses ambil data
            new FavouriteMovieActivity.LoadMovieAsync(this, this).execute();
        } else {
            ArrayList<MovieItems> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (list != null) {
                progressBar.setVisibility(View.INVISIBLE);
                adapter.setListMovieFav(list);
            }
        }
    }


    private void showSnackbarMessage(String message) {
        Snackbar.make(rvMovieFav, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void preExecute() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void postExecute(ArrayList<MovieItems> movies) {
        progressBar.setVisibility(View.INVISIBLE);
        if (movies.size() > 0) {
            adapter.setListMovieFav(movies);
        } else {
            adapter.setListMovieFav(new ArrayList<MovieItems>());
            showSnackbarMessage("Tidak ada data saat ini");
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, adapter.getListMoviesFav());
        Log.d(TAG, "onSaveInstanceState: Sini COKKK");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_change_settings) {
            Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(mIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        this.menu = menu;
        menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_more_vert_black_24dp));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //movieHelper.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DetailMovieActivity.REQUEST_UPDATE) {
            if (resultCode == DetailMovieActivity.RESULT_DELETE) {
                int position = data.getIntExtra(DetailMovieActivity.EXTRA_POS, 0);
                Log.d(TAG, "onActivityResult: MASUK DELETE " + position);
                adapter.removeItem(position);

            }
        }
    }

    private static class LoadMovieAsync extends AsyncTask<Void, Void, ArrayList<MovieItems>> {

        private final WeakReference<Context> weakContext;
        //        private final WeakReference<MovieHelper> weakMovieHelper;
        private final WeakReference<LoadMovieCallback> weakCallback;

        public LoadMovieAsync(Context context, LoadMovieCallback callback) {
            weakContext = new WeakReference<>(context);
            //weakMovieHelper = new WeakReference<>(movieHelper);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<MovieItems> doInBackground(Void... voids) {
            Context context = weakContext.get();
            Cursor dataCursor = context.getContentResolver().query(CONTENT_URI, null, null, null, null);
            return MappingHelper.mapCursorToArrayListMovie(dataCursor);
        }

        @Override
        protected void onPostExecute(ArrayList<MovieItems> movieItems) {
            super.onPostExecute(movieItems);
            weakCallback.get().postExecute(movieItems);
        }
    }

    public static class DataObserver extends ContentObserver {
        final Context context;

        public DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new LoadMovieAsync(context, (LoadMovieCallback) context).execute();
        }
    }


}
