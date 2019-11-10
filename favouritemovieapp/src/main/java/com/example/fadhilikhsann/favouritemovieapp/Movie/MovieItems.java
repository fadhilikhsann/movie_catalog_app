package com.example.fadhilikhsann.favouritemovieapp.Movie;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MovieItems implements Parcelable {
    public static final Creator<MovieItems> CREATOR = new Creator<MovieItems>() {
        @Override
        public MovieItems createFromParcel(Parcel source) {
            return new MovieItems(source);
        }

        @Override
        public MovieItems[] newArray(int size) {
            return new MovieItems[size];
        }
    };
    private int id, popularity;
    private String title;
    private String poster;
    private double vote;
    private String overview;
    private String release;
    private String genre;

    public MovieItems(String id, String title, String poster) {
        this.id = Integer.parseInt(id);
        this.title = title;
        this.poster = poster;
    }

    public MovieItems(JSONObject object) {
        try {
            String genre = "";
            int id = object.getInt("id");
            int popularity = object.getInt("popularity");
            String title = object.getString("title");
            String poster = object.getString("poster_path");
            double vote = object.getDouble("vote_average");
            JSONArray list = object.getJSONArray("genre_ids");
            String overview = object.getString("overview");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(object.getString("release_date"));
            sdf = new SimpleDateFormat("yyyy");
            String release = sdf.format(date);

            this.id = id;
            this.popularity = popularity;
            this.title = title;
            this.poster = poster;
            this.vote = vote;
            this.overview = overview;
            this.release = release;
            this.genre = genre;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected MovieItems(Parcel in) {
        this.id = in.readInt();
        this.popularity = in.readInt();
        this.title = in.readString();
        this.poster = in.readString();
        this.vote = in.readDouble();
        this.overview = in.readString();
        this.release = in.readString();
        this.genre = in.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public double getVote() {
        return vote;
    }

    public void setVote(double vote) {
        this.vote = vote;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.popularity);
        dest.writeString(this.title);
        dest.writeString(this.poster);
        dest.writeDouble(this.vote);
        dest.writeString(this.overview);
        dest.writeString(this.release);
        dest.writeString(this.genre);
    }
}
