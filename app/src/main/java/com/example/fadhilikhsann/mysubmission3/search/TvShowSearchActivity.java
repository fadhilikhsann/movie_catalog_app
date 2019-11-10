package com.example.fadhilikhsann.mysubmission3.search;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fadhilikhsann.mysubmission3.R;
import com.example.fadhilikhsann.mysubmission3.TvShow.DetailTvShowActivity;
import com.example.fadhilikhsann.mysubmission3.TvShow.TvShowItems;
import com.example.fadhilikhsann.mysubmission3.TvShow.TvShowViewModel;
import com.example.fadhilikhsann.mysubmission3.adapter.TvShowAdapter;

import java.util.ArrayList;

public class TvShowSearchActivity extends AppCompatActivity {
    public static final String EXTRA_DATA = "data cari";
    private static final String TAG = "Search";
    private static final String EXTRA_STATE = "Extra State";
    private static final String EXTRA_CEK = "Extra Cek";
    private String cek = "";
    private Menu menu;
    private EditText edtCari;
    private ImageView imgCari;
    private TvShowAdapter adapter;
    private ProgressBar progressBar;
    private TvShowViewModel tvShowViewModel;
    private Observer<ArrayList<TvShowItems>> getTvShow = new Observer<ArrayList<TvShowItems>>() {
        @Override
        public void onChanged(ArrayList<TvShowItems> items) {
            if (items != null) {
                Log.d(TAG, "Observe: " + items.size());
                adapter.setData(items);
                if (adapter.getItemCount() > 0) {
                    showLoading(false);
                } else {
                    showLoading(true);
                }
            }
        }
    };

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        adapter = new TvShowAdapter();
        adapter.notifyDataSetChanged();
        final String lang = getResources().getString(R.string.bahasa_api);
        final String cari = getIntent().getStringExtra(EXTRA_DATA);
        edtCari = findViewById(R.id.edtCari);
        imgCari = findViewById(R.id.imgCari);
        edtCari.setText(cari);


        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        tvShowViewModel = ViewModelProviders.of(this).get(TvShowViewModel.class);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        showLoading(true);
        //Perlu diganti

        //Perlu diganti

        imgCari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cek.equals(edtCari.getText().toString())) {
                    Toast.makeText(TvShowSearchActivity.this, getResources().getString(R.string.message_already), Toast.LENGTH_SHORT).show();
                } else {
                    tvShowViewModel.removeData();
                    adapter.removeListTvShow();
                    showLoading(true);
                    dataSet(lang, edtCari.getText().toString());
                    cek = edtCari.getText().toString();
                }

            }
        });


        if (savedInstanceState == null) {
            Log.d(TAG, "onNGADEK: ngadekcok");
            // proses ambil data
            cek = edtCari.getText().toString();
            dataSet(lang, cari);
        } else {
            cek = savedInstanceState.getString(EXTRA_CEK);
            Log.d(TAG, "onPUTAR: ada " + cek);
            ArrayList<TvShowItems> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (list != null) {
                showLoading(false);
                adapter.setListTvShow(list);
            }
        }


        adapter.setOnItemClickCallback(new TvShowAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(TvShowItems data) {
                showSelected(data);
            }
        });


    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, adapter.getListTvShow());
        outState.putString(EXTRA_CEK, cek);
        Log.d(TAG, "onSaveInstanceState: Sini COKKK");
    }

    public void dataSet(String lang, String cari) {
        tvShowViewModel.getTvShow().observe(this, getTvShow);
        tvShowViewModel.setTvShow(lang, cari);
    }

    private void showSelected(TvShowItems data) {
        Intent moveWithObjectIntent = new Intent(TvShowSearchActivity.this, DetailTvShowActivity.class);
        moveWithObjectIntent.putExtra(DetailTvShowActivity.EXTRA_ID, String.valueOf(data.getId()));
        startActivity(moveWithObjectIntent);
    }

    private void showLoading(boolean b) {
        if (b) {
            Log.d(TAG, "showLoading: visible");
            progressBar.setVisibility(View.VISIBLE);
        } else {
            Log.d(TAG, "showLoading: gone");
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_change_settings) {
            Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(mIntent);
        } else if (item.getItemId() == android.R.id.home) {
            finish();
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
}