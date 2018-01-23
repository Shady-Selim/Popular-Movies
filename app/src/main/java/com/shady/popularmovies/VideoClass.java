package com.shady.popularmovies;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Shady on 9/6/2017.
 */

class VideoClass {
    @Expose
    public String id;
    @SerializedName("iso_639_1")
    @Expose
    public String iso6391;
    @SerializedName("iso_3166_1")
    @Expose
    public String iso31661;
    @Expose
    public String key;
    @Expose
    public String name;
    @Expose
    public String site;
    @Expose
    public Integer size;
    @Expose
    public String type;
}
