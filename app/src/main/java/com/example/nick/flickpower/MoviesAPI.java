package com.example.nick.flickpower;

import com.example.nick.flickpower.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by nick on 10/16/16.
 */

public abstract class MoviesAPI {

    public void fetchPopularMovies() {
        OkHttpClient client = new OkHttpClient();

        String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

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

                JSONArray movieJsonResults = null;
                try {
                    String responseData = response.body().string();
                    JSONObject json = new JSONObject(responseData);
                    movieJsonResults = json.getJSONArray("results");
                    fetchedResult(Movie.fromJSONArray(movieJsonResults));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                //super.onFailure(call, e);
            }
        });

    }

    abstract void fetchedResult(ArrayList<Movie> movieList);
}
