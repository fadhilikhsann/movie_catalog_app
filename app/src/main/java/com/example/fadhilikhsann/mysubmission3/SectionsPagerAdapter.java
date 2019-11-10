package com.example.fadhilikhsann.mysubmission3;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.fadhilikhsann.mysubmission3.Movie.MovieFragment;
import com.example.fadhilikhsann.mysubmission3.TvShow.TvShowFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
    private static final String TAG = "MainActivity";
    private final Context mContext;
    private String dataCari;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {

        super(fm);
        mContext = context;
        Log.d(TAG, "Masuk Seccccc" + this.dataCari + " gadapat");
    }

    public String getDataCari() {
        return dataCari;
    }

    public void setDataCari(String dataCari) {
        this.dataCari = dataCari;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (position) {
            case 0:
                MovieFragment mf = new MovieFragment();
                Bundle dataCari = new Bundle();
                dataCari.putString("data_cari", getDataCari());
                Log.d(TAG, "getItem: Hmmmmm");
                mf.setArguments(dataCari);
                return mf;
            case 1:
                return new TvShowFragment();
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }


    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }

}