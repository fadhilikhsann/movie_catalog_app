package com.example.fadhilikhsann.mysubmission3.favourite;

import static androidx.constraintlayout.widget.Constraints.TAG;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fadhilikhsann.mysubmission3.R;
import com.example.fadhilikhsann.mysubmission3.TvShow.DetailTvShowActivity;
import com.example.fadhilikhsann.mysubmission3.TvShow.TvShowItems;
import com.example.fadhilikhsann.mysubmission3.adapter.TvShowFavAdapter;
import com.example.fadhilikhsann.mysubmission3.db.TvShowHelper;
import com.example.fadhilikhsann.mysubmission3.helper.MappingHelper;
import com.google.android.material.snackbar.Snackbar;

import java.lang.ref.WeakReference;
import java.sql.SQLException;
import java.util.ArrayList;

interface LoadTvShowCallback {
    void preExecute();

    void postExecute(ArrayList<TvShowItems> notes);
}

public class FavouriteTvShowActivity extends AppCompatActivity implements LoadTvShowCallback {
    private static final String EXTRA_STATE = "EXTRA_STATE";
    private ProgressBar progressBar;
    private RecyclerView rvTvShowFav;
    private TvShowFavAdapter adapter;
    private TvShowHelper tvShowHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        progressBar = findViewById(R.id.progressBar);
        rvTvShowFav = findViewById(R.id.recyclerView);

        rvTvShowFav.setLayoutManager(new LinearLayoutManager(this));
        rvTvShowFav.setHasFixedSize(true);
        adapter = new TvShowFavAdapter(this);
        rvTvShowFav.setAdapter(adapter);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.favTvShow);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        tvShowHelper = TvShowHelper.getInstance(getApplicationContext());
        try {
            tvShowHelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        if (savedInstanceState == null) {
            // proses ambil data
            Log.d(TAG, "onCreate: saved: Null");
            new FavouriteTvShowActivity.LoadTvShowAsync(tvShowHelper, this).execute();


        } else {
            Log.d(TAG, "onCreate: saved: Not Null");
            ArrayList<TvShowItems> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (list != null) {
                Log.d(TAG, "onCreate: masuk adapt list");
                progressBar.setVisibility(View.INVISIBLE);
                adapter.setListTvShowFav(list);
                //dbTaken=true;
            }
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Favourite TV Shows");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSnackbarMessage(String message) {
        Snackbar.make(rvTvShowFav, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void preExecute() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: prog visib PRE");
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void postExecute(ArrayList<TvShowItems> tvShow) {
        Log.d(TAG, "postExecute: invis prog");
        progressBar.setVisibility(View.INVISIBLE);
        if (tvShow.size() > 0) {
            adapter.setListTvShowFav(tvShow);
        } else {
            adapter.setListTvShowFav(new ArrayList<TvShowItems>());
            showSnackbarMessage("Tidak ada data saat ini");
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, adapter.getListTvShowFav());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tvShowHelper.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DetailTvShowActivity.REQUEST_UPDATE) {
            if (resultCode == DetailTvShowActivity.RESULT_DELETE) {
                int position = data.getIntExtra(DetailTvShowActivity.EXTRA_POS, 0);
                Log.d(TAG, "onActivityResult: MASUK DELETE " + position);
                adapter.removeItem(position);

            }
        }
    }

    private static class LoadTvShowAsync extends AsyncTask<Void, Void, ArrayList<TvShowItems>> {

        private final WeakReference<TvShowHelper> weakTvShowHelper;
        private final WeakReference<LoadTvShowCallback> weakCallback;

        public LoadTvShowAsync(TvShowHelper tvShowHelper, LoadTvShowCallback callback) {
            weakTvShowHelper = new WeakReference<>(tvShowHelper);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<TvShowItems> doInBackground(Void... voids) {
            Cursor dataCursor = weakTvShowHelper.get().queryAll();
            return MappingHelper.mapCursorToArrayListTvShow(dataCursor);
        }

        @Override
        protected void onPostExecute(ArrayList<TvShowItems> tvShowItems) {
            super.onPostExecute(tvShowItems);
            weakCallback.get().postExecute(tvShowItems);
        }
    }
}
