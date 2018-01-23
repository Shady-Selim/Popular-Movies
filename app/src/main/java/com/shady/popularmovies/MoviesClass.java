package com.shady.popularmovies;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Shady on 8/16/2017.
 */

// used http://www.jsonschema2pojo.org/
class MoviesClass {
    @Expose
    public Integer page;
    @SerializedName("total_results")
    @Expose
    public Integer totalResults;
    @SerializedName("total_pages")
    @Expose
    public Integer totalPages;
    @Expose
    public List<MovieClass> results = null;
}
