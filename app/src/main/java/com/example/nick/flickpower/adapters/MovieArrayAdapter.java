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


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = getItem(position);

        // check if existing view is being reused
        if (convertView == null) {
            int type = getItemViewType(position);
            convertView = getInflatedLayoutForType(type);
            /*
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_movie, parent, false);*/
        }

        int orientation = getContext().getResources().getConfiguration().orientation;

        if (movie.popularity == Movie.Popularity.LOW) {
            // find the image view
            ImageView ivImage = (ImageView) convertView.findViewById(R.id.ivMovieImage);
            // clear out the image from convertView
            ivImage.setImageResource(0);

            TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            TextView tvOverview = (TextView) convertView.findViewById(R.id.tvOverview);
            RatingBar rbMovieScore = (RatingBar) convertView.findViewById(R.id.rbMovieRating);

            // populate data
            tvTitle.setText(movie.getOriginalTitle());
            tvOverview.setText(movie.getOverview());
            rbMovieScore.setRating(movie.getStarRating());

            // TODO: Try to display score like <big>4.34</big> /10
            //TextView tvScore = (TextView) convertView.findViewById(R.id.tvScore);
            //Spannable scoreText = new SpannableString(movie.getScore().toString());
            //tvScore.setText(scoreText, TextView.BufferType.SPANNABLE);

            String imagePath;

            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                imagePath = movie.getPosterPath();
                Log.d("DEBUG", "Portrait mode image");
                // only apply corner transformation on portrain images
                Picasso.with(getContext()).load(imagePath).fit().centerCrop().placeholder(R.drawable.movie_placeholder).error(R.drawable.movie_placeholder).transform(new RoundedCornersTransformation(16, 16)).into(ivImage);

            }
            // landscape mode
            else {
                imagePath = movie.getBackdropPath();
                Picasso.with(getContext()).load(imagePath).fit().centerCrop().placeholder(R.drawable.movie_placeholder).error(R.drawable.movie_placeholder).into(ivImage);

                Log.d("DEBUG", "Landscape mode image");
            }
            Log.d("DEBUG", imagePath);
            // Picasso.with(getContext()).load(movie.getPosterPath()).into(ivImage);

        }
        else if (movie.popularity == Movie.Popularity.HIGH) {
            // find the image view
            ImageView ivImage = (ImageView) convertView.findViewById(R.id.ivMovieImage);
            // clear out the image from convertView
            ivImage.setImageResource(0);

            TextView tvTitle = (TextView) convertView.findViewById(R.id.tvPopularTitle);
            //TextView tvOverview = (TextView) convertView.findViewById(R.id.tvOverview);
            //TextView tvScore = (TextView) convertView.findViewById(R.id.tvScore);

            // populate data
            tvTitle.setText(movie.getOriginalTitle());
            //tvOverview.setText(movie.getOverview());
            //tvScore.setText(movie.getScore().toString());

            String imagePath = movie.getBackdropPath();

            Log.d("DEBUG","Popular movie");
            //int width = convertView.getWidth();

            VignetteFilterTransformation vignetteTransform = new VignetteFilterTransformation(getContext(), new PointF(0.5f, 0.5f), new float[] { 0.0f, 0.0f, 0.0f }, 0f, 0.75f);
            Picasso.with(getContext()).load(imagePath).transform(vignetteTransform).into(ivImage);
            //Picasso.with(getContext()).load(imagePath).resize(width,0).placeholder(R.drawable.movie_placeholder).error(R.drawable.movie_placeholder).into(ivImage);
        }

        return convertView;
    }
}
