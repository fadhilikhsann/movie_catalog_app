package com.example.fadhilikhsann.mysubmission3.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.fadhilikhsann.mysubmission3.CustomOnItemClickListener;
import com.example.fadhilikhsann.mysubmission3.MainActivity;
import com.example.fadhilikhsann.mysubmission3.R;
import com.example.fadhilikhsann.mysubmission3.TvShow.DetailTvShowActivity;
import com.example.fadhilikhsann.mysubmission3.TvShow.TvShowItems;
import com.example.fadhilikhsann.mysubmission3.db.TvShowHelper;

import java.sql.SQLException;
import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.example.fadhilikhsann.mysubmission3.TvShow.DetailTvShowActivity.REQUEST_UPDATE;

public class TvShowFavAdapter extends RecyclerView.Adapter<TvShowFavAdapter.TvShowFavViewHolder> {
    private ArrayList<TvShowItems> listTvShowFav = new ArrayList<>();
    private Activity activity;


    public TvShowFavAdapter(Activity activity) {
        this.activity = activity;
    }

    public ArrayList<TvShowItems> getListTvShowFav() {
        return listTvShowFav;
    }

    public void setListTvShowFav(ArrayList<TvShowItems> listTvShowFav) {
        if (listTvShowFav.size() > 0) {
            this.listTvShowFav.clear();
        }
        this.listTvShowFav.addAll(listTvShowFav);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        this.listTvShowFav.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listTvShowFav.size());
        Log.d(TAG, "Kedelete cuy");
    }

    @NonNull
    @Override
    public TvShowFavAdapter.TvShowFavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fav, parent, false);
        return new TvShowFavViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TvShowFavAdapter.TvShowFavViewHolder holder, final int position) {
        holder.tvJudul.setText(listTvShowFav.get(position).getTitle());
        holder.tvId.setText(String.valueOf(listTvShowFav.get(position).getId()));
        Glide.with(holder.itemView.getContext())
                .load("https://image.tmdb.org/t/p/w185/" + listTvShowFav.get(position).getPoster())
                .apply(new RequestOptions().override(300, 600))
                .into(holder.imgFoto);
        holder.cvTvShowFav.setOnClickListener(new CustomOnItemClickListener(position, new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {

                Intent moveWithObjectIntent = new Intent(activity, DetailTvShowActivity.class);
                moveWithObjectIntent.putExtra(DetailTvShowActivity.EXTRA_ID, String.valueOf(listTvShowFav.get(position).getId()));
                moveWithObjectIntent.putExtra(DetailTvShowActivity.EXTRA_POS, position);
                Log.d(TAG, "onItemClicked: Ke Detail " + position);
                activity.startActivityForResult(moveWithObjectIntent, REQUEST_UPDATE);

            }
        }));
        holder.imgCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TvShowHelper tvShowHelper = TvShowHelper.getInstance(activity);
                try {
                    tvShowHelper.open();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                long result = tvShowHelper.deleteById(String.valueOf(listTvShowFav.get(position).getId()));
                if (result > 0) {
                    Toast.makeText(activity, "Success delete " + listTvShowFav.get(position).getTitle() + " from favourite!", Toast.LENGTH_SHORT).show();
                    removeItem(position);
                } else {
                    Toast.makeText(activity, "Fail delete " + listTvShowFav.get(position).getTitle() + " from favourite!", Toast.LENGTH_SHORT).show();
                }
                MainActivity.edit_fav_widget = true;
            }
        });
    }


    @Override
    public int getItemCount() {
        return listTvShowFav.size();
    }

    public class TvShowFavViewHolder extends RecyclerView.ViewHolder {
        final TextView tvJudul, tvId;
        final ImageView imgFoto, imgCross;
        final CardView cvTvShowFav;

        public TvShowFavViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJudul = itemView.findViewById(R.id.tvJudul);
            tvId = itemView.findViewById(R.id.tvId);
            imgFoto = itemView.findViewById(R.id.imgFoto);
            imgCross = itemView.findViewById(R.id.imgCross);
            cvTvShowFav = itemView.findViewById(R.id.cv_item_note);
        }
    }

}
