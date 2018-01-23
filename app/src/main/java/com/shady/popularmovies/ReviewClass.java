package com.shady.popularmovies;

import com.google.gson.annotations.Expose;

/**
 * Created by Shady on 9/6/2017.
 */

class ReviewClass {
    @Expose
    public String id;
    @Expose
    public String author;
    @Expose
    public String content;
    @Expose
    public String url;
}
