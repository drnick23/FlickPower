package com.example.nick.flickpower;

import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.nick.flickpower.models.Movie;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import jp.wasabeef.picasso.transformations.BlurTransformation;
import jp.wasabeef.picasso.transformations.GrayscaleTransformation;
import jp.wasabeef.picasso.transformations.gpu.VignetteFilterTransformation;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Movie movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra("movie"));
        Log.d("DEBUG", movie.getOriginalTitle());

        TextView title = (TextView) findViewById(R.id.tvTitle);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.rbMovieScore);
        ImageView backdropImage = (ImageView) findViewById(R.id.ivBackdrop);
        final ImageView moviePlayImage = (ImageView) findViewById(R.id.ivMovieImage);
        final ScrollView scrollView = (ScrollView) findViewById(R.id.svSynopsis);


        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                Log.d("DEBUG", "scroll changed: " + scrollView.getScrollY());
                int scrollY = scrollView.getScrollY(); // For ScrollView
                moviePlayImage.setAlpha((float) (200 - scrollY)/200);
                //Log.d("DEBUG",String.format("scrolled:%d", scrollY));
                // DO SOMETHING WITH THE SCROLL COORDINATES
            }
        });

        title.setText(movie.getOriginalTitle());

        // convert our movie score out of 10 into a float value out of 5
        ratingBar.setRating(movie.getStarRating());

        String imagePath = movie.getPosterPath();

        GrayscaleTransformation grayTransform = new GrayscaleTransformation();
        BlurTransformation blurTransform = new BlurTransformation(this, 25, 1);
        VignetteFilterTransformation vignetteTransform = new VignetteFilterTransformation(this, new PointF(0.5f, 0.5f), new float[] { 0.0f, 0.0f, 0.0f }, 0f, 0.75f);

        Picasso.with(this).load(imagePath).fit().centerCrop().transform(grayTransform).transform(blurTransform).transform(vignetteTransform).into(backdropImage);

        Picasso.with(this).load(movie.getBackdropPath()).fit().centerCrop().into(moviePlayImage);
    }
}
