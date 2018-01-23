package com.shady.popularmovies;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by Shady on 9/6/2017.
 */

public class VideosClass {
    @Expose
    public Integer id;
    @Expose
    public List<VideoClass> results = null;
}
