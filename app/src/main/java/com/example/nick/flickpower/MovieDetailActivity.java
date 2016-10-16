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

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.BlurTransformation;
import jp.wasabeef.picasso.transformations.GrayscaleTransformation;
import jp.wasabeef.picasso.transformations.gpu.VignetteFilterTransformation;

public class MovieDetailActivity extends AppCompatActivity {

    @BindView(R.id.tvTitle) TextView title;
    @BindView(R.id.tvSynopsis) TextView synopsis;
    @BindView(R.id.rbMovieScore) RatingBar ratingBar;
    @BindView(R.id.ivBackdrop) ImageView backdropImage;
    @BindView(R.id.ivMovieImage) ImageView moviePlayImage;
    @BindView(R.id.svSynopsis) ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Movie movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra("movie"));
        Log.d("DEBUG", movie.getOriginalTitle());

        // butterknife does the bindings for us, let's set the contents from our movie file
        ButterKnife.bind(this);
        title.setText(movie.getOriginalTitle());
        synopsis.setText(movie.getOverview());
        ratingBar.setRating(movie.getStarRating());

        // unfortunately it doesn't seem like ButterKnife has an on scroll changed listener...
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                // on scroll, adjust alpha of top image in scroll to fade away as we scroll up
                Log.d("DEBUG", "scroll changed: " + scrollView.getScrollY());
                int scrollY = scrollView.getScrollY(); // For ScrollView
                moviePlayImage.setAlpha((float) (200 - scrollY)/200);
            }
        });


        // get our backdrop image, that we grayscale, blur, and vignette to make the white
        // text readable above it
        GrayscaleTransformation grayTransform = new GrayscaleTransformation();
        BlurTransformation blurTransform = new BlurTransformation(this, 25, 1);
        VignetteFilterTransformation vignetteTransform = new VignetteFilterTransformation(this, new PointF(0.5f, 0.5f), new float[] { 0.0f, 0.0f, 0.0f }, 0f, 0.75f);
        Picasso.with(this).load(movie.getPosterPath()).fit().centerCrop().transform(grayTransform).transform(blurTransform).transform(vignetteTransform).into(backdropImage);

        // and finally load in the poster view to the foreground that will be blended when scrolled.
        Picasso.with(this).load(movie.getBackdropPath()).fit().centerCrop().into(moviePlayImage);
    }
}
