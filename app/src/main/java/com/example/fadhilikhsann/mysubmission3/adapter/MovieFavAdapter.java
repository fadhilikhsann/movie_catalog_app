package com.example.fadhilikhsann.mysubmission3.adapter;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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
import com.example.fadhilikhsann.mysubmission3.Movie.DetailMovieActivity;
import com.example.fadhilikhsann.mysubmission3.Movie.MovieItems;
import com.example.fadhilikhsann.mysubmission3.R;
import com.example.fadhilikhsann.mysubmission3.helper.MappingHelper;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.example.fadhilikhsann.mysubmission3.Movie.DetailMovieActivity.REQUEST_UPDATE;
import static com.example.fadhilikhsann.mysubmission3.db.DatabaseContract_Movie.CONTENT_URI;

public class MovieFavAdapter extends RecyclerView.Adapter<MovieFavAdapter.MovieFavViewHolder> {
    private ArrayList<MovieItems> listMovieFav = new ArrayList<>();
    private Activity activity;
    private Uri uriWithId;
    private MovieItems movieItems;


    public MovieFavAdapter(Activity activity) {
        this.activity = activity;
    }

    public ArrayList<MovieItems> getListMoviesFav() {
        return listMovieFav;
    }

    public void setListMovieFav(ArrayList<MovieItems> listMovieFav) {
        this.listMovieFav.clear();
        this.listMovieFav.addAll(listMovieFav);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        this.listMovieFav.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listMovieFav.size());
        Log.d(TAG, "Kedelete cuy");
    }

    @NonNull
    @Override
    public MovieFavAdapter.MovieFavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fav, parent, false);
        return new MovieFavViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieFavAdapter.MovieFavViewHolder holder, final int position) {
        holder.tvJudul.setText(listMovieFav.get(position).getTitle());
        holder.tvId.setText(String.valueOf(listMovieFav.get(position).getId()));
        Glide.with(holder.itemView.getContext())
                .load("https://image.tmdb.org/t/p/w185/" + listMovieFav.get(position).getPoster())
                .apply(new RequestOptions().override(300, 600))
                .into(holder.imgFoto);
        holder.cvMovieFav.setOnClickListener(new CustomOnItemClickListener(position, new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {

                Intent moveWithObjectIntent = new Intent(activity, DetailMovieActivity.class);
                moveWithObjectIntent.putExtra(DetailMovieActivity.EXTRA_ID, String.valueOf(listMovieFav.get(position).getId()));
                moveWithObjectIntent.putExtra(DetailMovieActivity.EXTRA_POS, position);
                Log.d(TAG, "onItemClicked: Ke Detail " + position);
                activity.startActivityForResult(moveWithObjectIntent, REQUEST_UPDATE);

            }
        }));
        holder.imgCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                uriWithId = Uri.parse(CONTENT_URI + "/" + listMovieFav.get(position).getId());
                if (uriWithId != null) {
                    Cursor cursor = activity.getContentResolver().query(uriWithId, null, null, null, null);
                    if (cursor != null) {
                        Log.d("DetailMovieActivity", "setDetail: " + cursor.getCount());
                        movieItems = MappingHelper.mapCursorToObject(cursor);
                        cursor.close();
                    }
                }
                long result = activity.getContentResolver().delete(uriWithId, null, null);

                if (result > 0) {
                    Toast.makeText(activity, "Success delete " + listMovieFav.get(position).getTitle() + " from favourite!", Toast.LENGTH_SHORT).show();
                    removeItem(position);
                } else {
                    Toast.makeText(activity, "Fail delete " + listMovieFav.get(position).getTitle() + " from favourite!", Toast.LENGTH_SHORT).show();
                }
                MainActivity.edit_fav_widget = true;
            }
        });
    }


    @Override
    public int getItemCount() {
        return listMovieFav.size();
    }

    public class MovieFavViewHolder extends RecyclerView.ViewHolder {
        final TextView tvJudul, tvId;
        final ImageView imgFoto, imgCross;
        final CardView cvMovieFav;

        public MovieFavViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJudul = itemView.findViewById(R.id.tvJudul);
            tvId = itemView.findViewById(R.id.tvId);
            imgFoto = itemView.findViewById(R.id.imgFoto);
            imgCross = itemView.findViewById(R.id.imgCross);
            cvMovieFav = itemView.findViewById(R.id.cv_item_note);
        }
    }

}
