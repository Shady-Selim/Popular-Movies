package com.shady.popularmovies;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Shady on 9/6/2017.
 */

public class ReviewsClass {
    @Expose
    public Integer id;
    @Expose
    public Integer page;
    @Expose
    public List<ReviewClass> results = null;
    @SerializedName("total_pages")
    @Expose
    public Integer totalPages;
    @SerializedName("total_results")
    @Expose
    public Integer totalResults;
}
