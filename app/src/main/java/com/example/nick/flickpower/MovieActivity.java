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

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


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

        // I get that this isn't the proper way to implement an movies API, but wanted to experiment with
        // Java interfaces as it's a new concept for me...(Java newbie here...), and wasn't sure how
        // to do a proper callback class.
        MoviesAPI api = new MoviesAPI() {
            @Override
            void fetchedResult(ArrayList<Movie> movieList) {
                movies.addAll(movieList);
                MovieActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        movieAdapter.notifyDataSetChanged();
                    }
                });
            }
        };
        api.fetchPopularMovies();

        /*
        // should be a singleton
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                // ... check for failure using `isSuccessful` before proceeding
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
                MovieActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONArray movieJsonResults = null;
                        try {
                            String responseData = response.body().string();
                            JSONObject json = new JSONObject(responseData);
                            movieJsonResults = json.getJSONArray("results");
                            movies.addAll(Movie.fromJSONArray(movieJsonResults));
                            movieAdapter.notifyDataSetChanged();
                            Log.d("DEBUG", movies.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }

            @Override
            public void onFailure(Call call, IOException e) {
                //super.onFailure(call, e);
            }
        });*/

        /*AsyncHttpClient client = new AsyncHttpClient();

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
        });*/
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
