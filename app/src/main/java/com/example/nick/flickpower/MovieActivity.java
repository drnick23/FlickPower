package com.example.nick.flickpower;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.nick.flickpower.adapters.MovieArrayAdapter;
import com.example.nick.flickpower.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class MovieActivity extends AppCompatActivity {

    @BindView(R.id.lvMovies) ListView lvItems;

    ArrayList<Movie> movies;
    MovieArrayAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

        ButterKnife.bind(this);

        movies = new ArrayList<>();
        movieAdapter = new MovieArrayAdapter(this, movies);
        lvItems.setAdapter(movieAdapter);
        // how do I use @OnItemClick from Butterknife without having to refer to the id in the view again?
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = movies.get(position);
                onMovieItemClick(movie);
            }
        });

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(url, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //
                // super.onSuccess(statusCode, headers, response);
                JSONArray movieJsonResults = null;

                try {
                    movieJsonResults = response.getJSONArray("results");
                    movies.addAll(Movie.fromJSONArray(movieJsonResults));
                    movieAdapter.notifyDataSetChanged();
                    Log.d("DEBUG", movies.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }


    // Intent navigations
    public void launchMovieDetailActivity(Movie movie) {
        Intent i = new Intent(MovieActivity.this, MovieDetailActivity.class);
        i.putExtra("movie", Parcels.wrap(movie));
        startActivity(i);
    }

    public void onMovieItemClick(Movie movie) {
        Log.d("DEBUG", "Picked movie");
        Log.d("DEBUG", movie.toString());
        launchMovieDetailActivity(movie);
    }

    public void onLayoutClick(View view) {
        Log.d("DEBUG", "Item was clicked");
    }
}
