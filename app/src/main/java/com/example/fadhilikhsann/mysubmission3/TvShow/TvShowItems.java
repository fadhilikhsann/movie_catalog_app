package com.example.fadhilikhsann.mysubmission3.TvShow;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TvShowItems implements Parcelable {
    public static final Parcelable.Creator<TvShowItems> CREATOR = new Parcelable.Creator<TvShowItems>() {
        @Override
        public TvShowItems createFromParcel(Parcel source) {
            return new TvShowItems(source);
        }

        @Override
        public TvShowItems[] newArray(int size) {
            return new TvShowItems[size];
        }
    };
    private String title;
    private String poster;
    private String release;
    private int id;
    private String overview;
    private String country;
    private Double vote;

    public TvShowItems(String title, String poster, String release, int id, String overview, String country, Double vote) {
        this.title = title;
        this.poster = poster;
        this.release = release;
        this.id = id;
        this.overview = overview;
        this.country = country;
        this.vote = vote;
    }

    public TvShowItems(String id, String title, String poster) {
        this.id = Integer.parseInt(id);
        this.title = title;
        this.poster = poster;
    }


    public TvShowItems(JSONObject object) {

        try {
            double vote = object.getDouble("vote_average");
            String title = object.getString("name");
            String poster = object.getString("poster_path");
            String overview = object.getString("overview");
            String country = object.getJSONArray("origin_country").getString(0);
            int id = object.getInt("id");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(object.getString("first_air_date"));
            sdf = new SimpleDateFormat("yyyy");
            String release = sdf.format(date);

            this.id = id;
            this.title = title;
            this.poster = poster;
            this.overview = overview;
            this.country = country;
            this.vote = vote;
            this.release = release;
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    protected TvShowItems(Parcel in) {
        this.title = in.readString();
        this.poster = in.readString();
        this.release = in.readString();
        this.id = in.readInt();
        this.overview = in.readString();
        this.country = in.readString();
        this.vote = (Double) in.readValue(Double.class.getClassLoader());
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Double getVote() {
        return vote;
    }

    public void setVote(Double vote) {
        this.vote = vote;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.poster);
        dest.writeString(this.release);
        dest.writeInt(this.id);
        dest.writeString(this.overview);
        dest.writeString(this.country);
        dest.writeValue(this.vote);
    }
}
