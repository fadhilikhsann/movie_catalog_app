package com.example.fadhilikhsann.mysubmission3.TvShow;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TvShowViewModel extends ViewModel {
    private static final String API_KEY = "0a5f7dacaa1737c927feb35a47fdf1dd";
    private MutableLiveData<ArrayList<TvShowItems>> listTvShow = new MutableLiveData<>();

    public LiveData<ArrayList<TvShowItems>> getTvShow() {
        return listTvShow;
    }

    public void removeData() {
        final ArrayList<TvShowItems> listItems = new ArrayList<>();
        listItems.clear();
        listTvShow.postValue(listItems);
    }


    public void setTvShow(final String lang, final String cari) {
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<TvShowItems> listItems = new ArrayList<>();
        String url = "https://api.themoviedb.org/3/discover/tv?api_key=" + API_KEY + "&language=" + lang;

        if (cari.isEmpty()) {
            url = "https://api.themoviedb.org/3/discover/tv?api_key=" + API_KEY + "&language=" + lang;
        } else {
            url = "https://api.themoviedb.org/3/search/tv?api_key=" + API_KEY + "&language=" + lang + "&query=" + cari;
        }


        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject tvShow = list.getJSONObject(i);
                        TvShowItems tvShowItems = new TvShowItems(tvShow);
                        listItems.add(tvShowItems);
                    }
                    listTvShow.postValue(listItems);
                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", error.getMessage());
            }
        });
    }


}
