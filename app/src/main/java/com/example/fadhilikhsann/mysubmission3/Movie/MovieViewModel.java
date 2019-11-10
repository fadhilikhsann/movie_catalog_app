package com.example.fadhilikhsann.mysubmission3.Movie;

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

public class MovieViewModel extends ViewModel {
    private static final String API_KEY = "0a5f7dacaa1737c927feb35a47fdf1dd";
    public static int raw;
    private MutableLiveData<ArrayList<MovieItems>> listMovie = new MutableLiveData<>();

    public LiveData<ArrayList<MovieItems>> getMovie() {
        return listMovie;
    }


    public void removeData() {
        final ArrayList<MovieItems> listItems = new ArrayList<>();
        listItems.clear();
        listMovie.postValue(listItems);
    }

    public void setMovie(final String lang, final String cari) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "";
        final ArrayList<MovieItems> listItems = new ArrayList<>();
        if (cari.isEmpty()) {
            url = "https://api.themoviedb.org/3/discover/movie?api_key=" + API_KEY + "&language=" + lang;
        } else {
            url = "https://api.themoviedb.org/3/search/movie?api_key=" + API_KEY + "&language=" + lang + "&query=" + cari;
        }
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject movie = list.getJSONObject(i);
                        MovieItems movieItems = new MovieItems(movie);
                        listItems.add(movieItems);
                    }
                    listMovie.postValue(listItems);
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
