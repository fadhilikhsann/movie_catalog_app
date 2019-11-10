package com.example.fadhilikhsann.favouritemovieapp.Movie;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.fadhilikhsann.favouritemovieapp.R;

import java.util.ArrayList;

public class DetailMovieActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "id";
    public static final String EXTRA_POS = "pos";
    public static final int REQUEST_UPDATE = 200;
    public static final int RESULT_DELETE = 301;
    public static final int RESULT_NOTHING = 69;
    TextView tvJudull, tvGenree, tvRatee, tvProduserr, tvOvervieww, tvRiliss, tvMinutee;
    ImageView imgBackk, imgPosterr, imgFavv;
    ProgressBar progressBar;
    private DetailMovieViewModel detailMovieViewModel;
    private boolean isDelete = false;
    private Uri uriWithId;
    private MovieItems movieItems;
    private Observer<ArrayList<DetailMovieItems>> getDetailMovie = new Observer<ArrayList<DetailMovieItems>>() {
        @Override
        public void onChanged(ArrayList<DetailMovieItems> items) {
            if (items != null) {
                setDetail(items);
                showLoading(false);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        tvJudull = findViewById(R.id.tvJudul);
        tvGenree = findViewById(R.id.tvGenre);
        tvRatee = findViewById(R.id.tvRate);
        tvProduserr = findViewById(R.id.tvProducer);
        tvRiliss = findViewById(R.id.tvRelease);
        tvOvervieww = findViewById(R.id.tvOverview);
        imgBackk = findViewById(R.id.imgBack);
        imgPosterr = findViewById(R.id.imgPoster);
        imgFavv = findViewById(R.id.imgFav);
        tvMinutee = findViewById(R.id.tvMinutes);
        progressBar = findViewById(R.id.progressBar);
        detailMovieViewModel = ViewModelProviders.of(this).get(DetailMovieViewModel.class);
        detailMovieViewModel.getDetailMovie().observe(this, getDetailMovie);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final String id = getIntent().getStringExtra(EXTRA_ID);


        findViewById(R.id.imageView5).setVisibility(View.GONE);
        findViewById(R.id.imageView6).setVisibility(View.GONE);
        findViewById(R.id.imageView7).setVisibility(View.GONE);
        findViewById(R.id.imgstrip).setVisibility(View.GONE);
        findViewById(R.id.imgstrip2).setVisibility(View.GONE);
        findViewById(R.id.imageView3).setVisibility(View.GONE);
        imgBackk.setVisibility(View.GONE);
        imgPosterr.setVisibility(View.GONE);
        imgFavv.setVisibility(View.GONE);
        tvJudull.setVisibility(View.GONE);
        tvGenree.setVisibility(View.GONE);
        tvRatee.setVisibility(View.GONE);
        tvProduserr.setVisibility(View.GONE);
        tvRiliss.setVisibility(View.GONE);
        tvOvervieww.setVisibility(View.GONE);
        tvMinutee.setVisibility(View.GONE);
        findViewById(R.id.textView12).setVisibility(View.GONE);
        findViewById(R.id.textView7).setVisibility(View.GONE);
        findViewById(R.id.textView10).setVisibility(View.GONE);
        showLoading(true);
        detailMovieViewModel.setDetail(id, getResources().getString(R.string.bahasa_api));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //movieHelper.close();
    }

    private void showLoading(boolean b) {
        if (b) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void setDetail(final ArrayList<DetailMovieItems> itemData) {
        Glide.with(getApplicationContext())
                .load("https://image.tmdb.org/t/p/w185/" + itemData.get(0).getTvPoster())
                .apply(new RequestOptions().override(300, 600))
                .into(imgPosterr);
        Glide.with(getApplicationContext())
                .load("https://image.tmdb.org/t/p/original/" + itemData.get(0).getTvBack())
                .apply(new RequestOptions().override(1200, 1200))
                .into(imgBackk);
        tvJudull.setText(itemData.get(0).getTvJudul() + " (" + itemData.get(0).getTvYear() + ")");
        tvGenree.setText(itemData.get(0).getTvGenre());
        tvRatee.setText(String.valueOf(itemData.get(0).getTvRating()));
        tvProduserr.setText(itemData.get(0).getTvProduser());
        tvRiliss.setText(itemData.get(0).getTvRilis());
        tvOvervieww.setText(itemData.get(0).getTvOverview());
        tvMinutee.setText(itemData.get(0).getTvDurasi() + " " + getResources().getString(R.string.minute));

        findViewById(R.id.progressBar).setVisibility(View.GONE);
        findViewById(R.id.imageView5).setVisibility(View.VISIBLE);
        findViewById(R.id.imageView6).setVisibility(View.VISIBLE);
        findViewById(R.id.imageView7).setVisibility(View.VISIBLE);
        findViewById(R.id.imgstrip).setVisibility(View.VISIBLE);
        findViewById(R.id.imgstrip2).setVisibility(View.VISIBLE);
        findViewById(R.id.imageView3).setVisibility(View.VISIBLE);
        imgBackk.setVisibility(View.VISIBLE);
        imgPosterr.setVisibility(View.VISIBLE);
        imgFavv.setVisibility(View.VISIBLE);
        tvJudull.setVisibility(View.VISIBLE);
        tvGenree.setVisibility(View.VISIBLE);
        tvRatee.setVisibility(View.VISIBLE);
        tvProduserr.setVisibility(View.VISIBLE);
        tvRiliss.setVisibility(View.VISIBLE);
        tvOvervieww.setVisibility(View.VISIBLE);
        tvMinutee.setVisibility(View.VISIBLE);
        findViewById(R.id.textView12).setVisibility(View.VISIBLE);
        findViewById(R.id.textView7).setVisibility(View.VISIBLE);
        findViewById(R.id.textView10).setVisibility(View.VISIBLE);
        imgFavv.setImageResource(R.drawable.ic_favorite_fix);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                /*
                int pos=getIntent().getIntExtra(EXTRA_POS,0);
                Intent intent = new Intent();
                intent.putExtra(EXTRA_POS, pos);
                if(pos != -1 && !isDelete){
                    setResult(RESULT_DELETE, intent);
                }
                else if(pos!=-1 && isDelete){
                    setResult(RESULT_NOTHING,intent);
                }
              */
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
