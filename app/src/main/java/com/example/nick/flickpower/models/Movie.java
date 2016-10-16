package com.example.nick.flickpower.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by nick on 10/11/16.
 */
@Parcel
public class Movie {

    public enum Popularity {
        LOW, HIGH
    }
    public Popularity popularity;

    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342%s", posterPath);
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w342%s", backdropPath);
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public Double getScore() {
        return score;
    }

    public float getStarRating() {
        // map movie score out of 10 to a rating out of 5
        return Float.parseFloat(String.valueOf(score/2));
    }

    String posterPath;
    String backdropPath;
    String originalTitle;
    String overview;
    Double score;
    int voteCount;

    // required by Parceler Library
    public Movie() {

    }

    public Movie(JSONObject jsonObject) throws JSONException {
        this.posterPath = jsonObject.getString("poster_path");
        this.backdropPath = jsonObject.getString("backdrop_path");
        this.originalTitle = jsonObject.getString("original_title");
        this.overview = jsonObject.getString("overview");
        this.score = jsonObject.getDouble("vote_average");
        this.voteCount = jsonObject.getInt("vote_count");
        if (this.score >= 5) {
            this.popularity = Popularity.HIGH;
        } else {
            this.popularity = Popularity.LOW;
        }
    }

    public static ArrayList<Movie> fromJSONArray(JSONArray array) {
        ArrayList<Movie> results = new ArrayList<>();

        for (int x = 0; x < array.length(); x++) {
            try {
                results.add(new Movie(array.getJSONObject(x)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }
}
