package com.example.fadhilikhsann.mysubmission3;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.fadhilikhsann.mysubmission3.Movie.MovieFragment;
import com.example.fadhilikhsann.mysubmission3.TvShow.TvShowFragment;
import com.example.fadhilikhsann.mysubmission3.search.MovieSearchActivity;
import com.example.fadhilikhsann.mysubmission3.search.TvShowSearchActivity;
import com.example.fadhilikhsann.mysubmission3.setting.MySettingPreference;
import com.example.fadhilikhsann.mysubmission3.widget.FavMovieWidget;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity implements MovieFragment.OnFragmentInteractionListener, TvShowFragment.OnFragmentInteractionListener {

    private static final String TAG = "MainActivity";
    public static boolean edit_fav_widget = true;
    public static boolean dailyReminder = false, releaseReminder = false;
    public static boolean forDaily = false, forRelease = false;
    private Menu menu;
    private EditText edtCari;
    private ImageView imgCari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        final ViewPager viewPager = findViewById(R.id.view_pager);


        edit_fav_widget = true;
        viewPager.setAdapter(sectionsPagerAdapter);
        edtCari = findViewById(R.id.edtCari);
        imgCari = findViewById(R.id.imgCari);

        if (savedInstanceState != null) {
            forDaily = savedInstanceState.getBoolean("forDaily");
            forRelease = savedInstanceState.getBoolean("forRelease");
        }

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        searchFor(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Toast.makeText(MainActivity.this,
                        "Selected page position: " + position, Toast.LENGTH_SHORT).show();
                if (position == 0) {
                    edtCari.setHint(getResources().getString(R.string.tvSearchMovie));
                    searchFor(0);
                } else if (position == 1) {
                    edtCari.setHint(getResources().getString(R.string.tvSearchTvShow));
                    searchFor(1);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onStop() {
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = manager.getAppWidgetIds(new ComponentName(this, FavMovieWidget.class));
        manager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stack_view);
        super.onStop();
    }

    @Override
    protected void onResume() {

        if (edit_fav_widget) {
            //Update widget jika ada penambahan atau pengurangan favorit
            AppWidgetManager manager = AppWidgetManager.getInstance(this);
            int[] appWidgetIds = manager.getAppWidgetIds(new ComponentName(this, FavMovieWidget.class));
            manager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stack_view);
            edit_fav_widget = false;
        }
        //Batas
        super.onResume();
    }

    @Override
    protected void onRestart() {
        edit_fav_widget = true;
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = manager.getAppWidgetIds(new ComponentName(this, FavMovieWidget.class));
        manager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stack_view);

        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putBoolean("remain_daily", forDaily);
        outState.putBoolean("remain_release", forRelease);
    }

    public void searchFor(final int arg) {
        imgCari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goCari;
                String data = edtCari.getText().toString();
                if (data.isEmpty()) {
                    Toast.makeText(MainActivity.this,
                            getResources().getString(R.string.message_error_search), Toast.LENGTH_SHORT).show();
                } else {
                    if (arg == 0) {
                        goCari = new Intent(MainActivity.this, MovieSearchActivity.class);
                        goCari.putExtra(MovieSearchActivity.EXTRA_DATA, data);
                        startActivity(goCari);
                    } else if (arg == 1) {
                        goCari = new Intent(MainActivity.this, TvShowSearchActivity.class);
                        goCari.putExtra(TvShowSearchActivity.EXTRA_DATA, data);
                        startActivity(goCari);
                    }
                }
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_change_settings) {
            Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(mIntent);
        } else if (item.getItemId() == R.id.action_reminder) {
            Intent mIntent = new Intent(MainActivity.this, MySettingPreference.class);
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
}