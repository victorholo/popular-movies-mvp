package com.example.popularmovies.ui.detail;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.popularmovies.R;
import com.example.popularmovies.data.models.movie.Movie;
import com.example.popularmovies.data.models.video.Video;
import com.example.popularmovies.utilities.MoviePosterUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

/**
 * Created by Victor Holotescu on 19-02-2018.
 */

public class VideosRecyclerAdapter extends RecyclerView.Adapter<VideosRecyclerAdapter.VideosAdapterViewHolder> {

    private final VideosAdapterOnClickHandler mVideoClickHandler;
    private List<Video> mVideos;

    private final Activity mActivity;

    public VideosRecyclerAdapter(Activity activity, VideosAdapterOnClickHandler clickHandler) {
        mVideoClickHandler = clickHandler;
        mActivity = activity;
    }

    @Override
    public VideosRecyclerAdapter.VideosAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mActivity).inflate(R.layout.video_item, parent, false);

        return new VideosAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideosRecyclerAdapter.VideosAdapterViewHolder holder, int position) {
        String videoKey = mVideos.get(position).getKey();
        String imgUrlString = getYoutubeImgUrlFromKey(videoKey);

        Picasso.with(mActivity)
                .load(imgUrlString)
                .error(R.drawable.video_placeholder)
                .placeholder(R.drawable.video_placeholder)
                .into(holder.videoImageView);
    }

    @Override
    public int getItemCount() {
        if (mVideos == null) return 0;
        return mVideos.size();
    }

    void swapVideos(List<Video> videos) {
        mVideos = videos;
        notifyDataSetChanged();
    }

    public interface VideosAdapterOnClickHandler {
        void onItemClicked(String videoKey);
    }

    public class VideosAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView videoImageView;
        public VideosAdapterViewHolder(View itemView) {
            super(itemView);
            videoImageView = itemView.findViewById(R.id.videoImageView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mVideoClickHandler.onItemClicked(mVideos.get(getAdapterPosition()).getKey());
        }
    }

    private String getYoutubeImgUrlFromKey(String videoKey) {
        return "http://img.youtube.com/vi/" + videoKey + "/0.jpg";
    }
}