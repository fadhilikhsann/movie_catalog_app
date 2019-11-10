package com.example.fadhilikhsann.mysubmission3.Movie;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class DetailMovieViewModel extends ViewModel {
    private static final String API_KEY = "0a5f7dacaa1737c927feb35a47fdf1dd";
    private MutableLiveData<ArrayList<DetailMovieItems>> mutDetailMovie = new MutableLiveData<>();

    LiveData<ArrayList<DetailMovieItems>> getDetailMovie() {
        return mutDetailMovie;
    }

    void setDetail(String id, String lang) {
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<DetailMovieItems> detailItems = new ArrayList<>();
        String url = "https://api.themoviedb.org/3/movie/" + id + "?api_key=" + API_KEY + "&language=" + lang;
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    DetailMovieItems detailMovieItems = new DetailMovieItems(responseObject);
                    detailItems.add(detailMovieItems);
                    mutDetailMovie.postValue(detailItems);
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
