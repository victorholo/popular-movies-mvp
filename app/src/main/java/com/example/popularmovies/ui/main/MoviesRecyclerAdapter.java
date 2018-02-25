package com.example.popularmovies.ui.main;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.popularmovies.R;
import com.example.popularmovies.data.models.movie.Movie;
import com.example.popularmovies.utilities.MoviePosterUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Victor Holotescu on 19-02-2018.
 */

public class MoviesRecyclerAdapter extends RecyclerView.Adapter<MoviesRecyclerAdapter.MoviesAdapterViewHolder> {

    private final MoviesAdapterOnClickHandler mMovieClickHandler;
    private List<Movie> mMovies;

    private final Activity mActivity;

    public MoviesRecyclerAdapter(Activity activity, MoviesAdapterOnClickHandler clickHandler) {
        mMovieClickHandler = clickHandler;
        mActivity = activity;
    }

    @Override
    public MoviesRecyclerAdapter.MoviesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mActivity).inflate(R.layout.movies_list_item, parent, false);

        return new MoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesRecyclerAdapter.MoviesAdapterViewHolder holder, int position) {
        byte[] cachedImage = mMovies.get(position).getCachedImage();
        if(cachedImage != null){
            Bitmap posterBitmap = MoviePosterUtils.getBitmapFromByteArray(cachedImage);
            holder.poster.setImageBitmap(posterBitmap);
        }else {
            String urlString = MoviePosterUtils.getSmallMoviePosterUrlString(mMovies.get(position).getPosterPath());
            Picasso.with(mActivity).load(urlString).into(holder.poster);
        }
    }

    @Override
    public int getItemCount() {
        if (mMovies == null) return 0;
        return mMovies.size();
    }

    void swapMovies(List<Movie> movies) {
            mMovies = movies;
            notifyDataSetChanged();
    }

    public interface MoviesAdapterOnClickHandler {
        void onItemClicked(int movieId);
    }

    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView poster;

        public MoviesAdapterViewHolder(View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.posterImageView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mMovieClickHandler.onItemClicked(mMovies.get(getAdapterPosition()).getId());
        }
    }

}