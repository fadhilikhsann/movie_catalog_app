package com.example.fadhilikhsann.mysubmission3.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.fadhilikhsann.mysubmission3.Movie.MovieItems;
import com.example.fadhilikhsann.mysubmission3.R;
import com.example.fadhilikhsann.mysubmission3.db.MovieHelper;

import java.sql.SQLException;
import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    MovieHelper movieHelper;
    private OnItemClickCallback onItemClickCallback;
    private ArrayList<MovieItems> mData = new ArrayList<>();

    public void setListMovie(ArrayList<MovieItems> listMovie) {
        if (listMovie.size() > 0) {
            this.mData.clear();
        }
        this.mData.addAll(listMovie);
        notifyDataSetChanged();
    }

    public ArrayList<MovieItems> getListMovies() {
        return mData;
    }

    public void removeListMovie() {
        mData.clear();
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public void setData(ArrayList<MovieItems> items) {
        mData.clear();
        mData.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_main, viewGroup, false);
        return new MovieViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieViewHolder holder, final int position) {
        holder.bind(mData.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(mData.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface OnItemClickCallback {
        void onItemClicked(MovieItems data);
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFoto;
        TextView tvJudul;
        TextView tvVote;
        TextView tvRelease;
        TextView tvOverview;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFoto = itemView.findViewById(R.id.imgFoto);
            tvJudul = itemView.findViewById(R.id.tvJudul);
            tvVote = itemView.findViewById(R.id.tvRating);
            tvRelease = itemView.findViewById(R.id.tvRelease);
            tvOverview = itemView.findViewById(R.id.tvOverview);

            movieHelper = MovieHelper.getInstance(itemView.getContext());
        }

        public void bind(MovieItems movieItems) {
            try {
                movieHelper.open();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Glide.with(itemView.getContext())
                    .load("https://image.tmdb.org/t/p/w185/" + movieItems.getPoster())
                    .apply(new RequestOptions().override(300, 600))
                    .into(imgFoto);
            tvRelease.setText(movieItems.getRelease());
            tvJudul.setText(movieItems.getTitle());
            tvVote.setText(String.valueOf(movieItems.getVote()));
            tvOverview.setText(movieItems.getOverview());
        }
    }
}