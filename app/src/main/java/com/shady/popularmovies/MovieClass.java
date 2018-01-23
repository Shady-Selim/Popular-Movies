package com.shady.popularmovies;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Shady on 8/16/2017.
 */

class MovieClass {
    @SerializedName("vote_count")
    @Expose
    public Integer voteCount;
    @Expose
    public Integer id;
    @Expose
    public Boolean video;
    @SerializedName("vote_average")
    @Expose
    public Double voteAverage;
    @Expose
    public String title;
    @Expose
    public Double popularity;
    @SerializedName("poster_path")
    @Expose
    public String posterPath;
    @SerializedName("original_language")
    @Expose
    public String originalLanguage;
    @SerializedName("original_title")
    @Expose
    public String originalTitle;
    @SerializedName("genre_ids")
    @Expose
    public List<Integer> genreIds = null;
    @SerializedName("backdrop_path")
    @Expose
    public String backdropPath;
    @Expose
    public Boolean adult;
    @Expose
    public String overview;
    @SerializedName("release_date")
    @Expose
    public String releaseDate;
}
