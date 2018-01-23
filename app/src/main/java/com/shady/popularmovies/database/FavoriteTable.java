package com.shady.popularmovies.database;


import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Shady on 9/5/2017.
 */
public class FavoriteTable {
    public static final String TABLE_FAVORITE = "favorite";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_MOVIE_ID = "movieID";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_RELEASE_DATE = "releaseDate";
    public static final String COLUMN_VOTE_AVERAGE = "voteAverage";
    public static final String COLUMN_OVERVIEW = "overview";
    public static final String COLUMN_POSTER_PATH = "posterPath";
    public static final String COLUMN_BACKDROP_PATH = "backdropPath";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_FAVORITE
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_MOVIE_ID + " text not null, "
            + COLUMN_TITLE + " text not null,"
            + COLUMN_RELEASE_DATE + " text not null,"
            + COLUMN_VOTE_AVERAGE + " text not null,"
            + COLUMN_OVERVIEW + " text not null,"
            + COLUMN_POSTER_PATH + " text not null,"
            + COLUMN_BACKDROP_PATH + " text not null"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITE);
        onCreate(database);
    }
}
