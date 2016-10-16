package com.example.nick.flickpower.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PointF;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.nick.flickpower.R;
import com.example.nick.flickpower.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import jp.wasabeef.picasso.transformations.gpu.VignetteFilterTransformation;

/**
 * Created by nick on 10/11/16.
 */
public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    public MovieArrayAdapter(Context context, List<Movie>movies) {
        super(context, android.R.layout.simple_list_item_1,movies);
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).popularity.ordinal();
    }

    // We show different view's based on movie's popularity
    @Override
    public int getViewTypeCount() {
        return Movie.Popularity.values().length;
    }

    private View getInflatedLayoutForType(int type) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        if (type == Movie.Popularity.HIGH.ordinal()) {
            return inflater.inflate(R.layout.item_popular_movie, null);
        } else if (type == Movie.Popularity.LOW.ordinal()) {
            return inflater.inflate(R.layout.item_movie, null);
        } else {
            return null;
        }
    }

    static class MovieItemHolder {
        @BindView(R.id.ivMovieImage) ImageView ivImage;
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.tvOverview) TextView tvOverview;
        @BindView(R.id.rbMovieRating) RatingBar rbMovieScore;

        public MovieItemHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class PopularMovieItemHolder {
        @BindView(R.id.ivMovieImage) ImageView ivImage;
        @BindView(R.id.tvPopularTitle) TextView tvTitle;

        public PopularMovieItemHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = getItem(position);

        // check if existing view is being reused
        if (convertView == null) {
            int type = getItemViewType(position);
            convertView = getInflatedLayoutForType(type);
        }

        // less popular movie's get the poster view with text on the side and a small rating showing.
        if (movie.popularity == Movie.Popularity.LOW) {

            MovieItemHolder holder = new MovieItemHolder(convertView);

            // clear out the image from convertView
            holder.ivImage.setImageResource(0);
            holder.tvTitle.setText(movie.getOriginalTitle());
            holder.tvOverview.setText(movie.getOverview());
            holder.rbMovieScore.setRating(movie.getStarRating());

            // we load different images and apply different effects depending on orientation
            int orientation = getContext().getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                Log.d("DEBUG", "Portrait mode image");
                String imagePath = movie.getPosterPath();
                // apply corner transformation on portrait images
                Picasso.with(getContext()).load(imagePath).fit().centerCrop().placeholder(R.drawable.movie_placeholder).error(R.drawable.movie_placeholder).transform(new RoundedCornersTransformation(16, 16)).into(holder.ivImage);
            }
            else { // landscape mode
                Log.d("DEBUG", "Landscape mode image");
                String imagePath = movie.getBackdropPath();
                Picasso.with(getContext()).load(imagePath).fit().centerCrop().placeholder(R.drawable.movie_placeholder).error(R.drawable.movie_placeholder).into(holder.ivImage);
            }

        }

        // popular movies get a full-wide image and popular star.
        else if (movie.popularity == Movie.Popularity.HIGH) {

            Log.d("DEBUG","Popular movie");
            PopularMovieItemHolder holder = new PopularMovieItemHolder(convertView);

            // clear out the image from convertView
            holder.ivImage.setImageResource(0);
            holder.tvTitle.setText(movie.getOriginalTitle());

            // load our full wide image and apply some extra effects
            String imagePath = movie.getBackdropPath();
            VignetteFilterTransformation vignetteTransform = new VignetteFilterTransformation(getContext(), new PointF(0.5f, 0.5f), new float[] { 0.0f, 0.0f, 0.0f }, 0f, 0.75f);
            Picasso.with(getContext()).load(imagePath).transform(vignetteTransform).into(holder.ivImage);
        }

        return convertView;
    }
}
