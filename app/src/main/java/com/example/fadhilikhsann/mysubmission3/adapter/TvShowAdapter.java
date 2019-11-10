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
import com.example.fadhilikhsann.mysubmission3.R;
import com.example.fadhilikhsann.mysubmission3.TvShow.TvShowItems;

import java.util.ArrayList;

public class TvShowAdapter extends RecyclerView.Adapter<TvShowAdapter.TvShowViewHolder> {
    private OnItemClickCallback onItemClickCallback;
    private ArrayList<TvShowItems> mData = new ArrayList<>();

    public ArrayList<TvShowItems> getListTvShow() {
        return mData;
    }

    public void setListTvShow(ArrayList<TvShowItems> listTvShow) {
        if (listTvShow.size() > 0) {
            this.mData.clear();
        }
        this.mData.addAll(listTvShow);
        notifyDataSetChanged();
    }

    public void removeListTvShow() {
        mData.clear();
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public void setData(ArrayList<TvShowItems> items) {
        mData.clear();
        mData.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TvShowViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_main, viewGroup, false);
        return new TvShowViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull final TvShowViewHolder holder, final int position) {
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
        void onItemClicked(TvShowItems data);
    }

    public class TvShowViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFoto;
        TextView tvJudul;
        TextView tvVote;
        TextView tvRelease;
        TextView tvOverview;

        public TvShowViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFoto = itemView.findViewById(R.id.imgFoto);
            tvJudul = itemView.findViewById(R.id.tvJudul);
            tvVote = itemView.findViewById(R.id.tvRating);
            tvRelease = itemView.findViewById(R.id.tvRelease);
            tvOverview = itemView.findViewById(R.id.tvOverview);


        }

        public void bind(final TvShowItems tvShowItems) {
            Glide.with(itemView.getContext())
                    .load("https://image.tmdb.org/t/p/w185/" + tvShowItems.getPoster())
                    .apply(new RequestOptions().override(300, 600))
                    .into(imgFoto);
            tvRelease.setText(tvShowItems.getRelease());
            tvJudul.setText(tvShowItems.getTitle());
            tvVote.setText(String.valueOf(tvShowItems.getVote()));
            tvOverview.setText(tvShowItems.getOverview());


        }
    }
}