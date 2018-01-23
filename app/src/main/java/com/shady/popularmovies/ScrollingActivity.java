package com.shady.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.shady.popularmovies.contentprovider.FavoriteContentProvider;
import com.shady.popularmovies.database.FavoriteTable;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shady.popularmovies.iAPI.retrofit;

public class ScrollingActivity extends AppCompatActivity {
    private List<VideoClass> list;
    private List<ReviewClass> listRev;
    private String mID, mReleaseDate, mVoteAverage, mOverview, mPosterPath, title, backdropPath;
    private boolean isFavorite = false;
    private Button favoiteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        favoiteBtn = (Button) findViewById(R.id.addToFavorite);

        final Intent intent = getIntent();
        if (intent.getExtras() != null) {
            title = intent.getStringExtra("title");
            backdropPath = intent.getStringExtra("backdropPath");
            mID = intent.getIntExtra("id",-1) + "";//321612
            mReleaseDate = intent.getStringExtra("releaseDate");
            mVoteAverage = intent.getStringExtra("voteAverage");
            mOverview = intent.getStringExtra("overview");
            mPosterPath = intent.getStringExtra("posterPath");

            setTitle(title);
            final CollapsingToolbarLayout toolbar_layout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
            Picasso.with(this).load(getString(R.string.details_thumb) + backdropPath).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    toolbar_layout.setBackground(new BitmapDrawable(bitmap));
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                }
                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                }
            });

            loadView(mID);

            TextView releaseDate = (TextView) findViewById(R.id.movieReleaseDate);
            releaseDate.setText(mReleaseDate);

            TextView voteAverage = (TextView) findViewById(R.id.movieVoteAverage);
            voteAverage.setText(mVoteAverage + " / 10");

            TextView overview = (TextView) findViewById(R.id.movieOverview);
            overview.setText(mOverview);

            ImageView posterPath = (ImageView) findViewById(R.id.movieImg);
            Picasso.with(getBaseContext())
                    .load(getString(R.string.thumb_path) + mPosterPath)
                    .into(posterPath);

            Cursor cursor = getContentResolver().query(FavoriteContentProvider.CONTENT_URI, null, FavoriteTable.COLUMN_MOVIE_ID + " = " + mID , null, null);
            if (cursor != null && cursor.moveToFirst()) {
                favoiteBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_favorite_gold_24dp),null,null,null);
                isFavorite = true;
            }
        }
    }

    private void loadView(String Id) {
        final ListView videos = (ListView) findViewById(R.id.movieVideos);
        final ListView reviews = (ListView) findViewById(R.id.movieReviews);
        final TextView vT = (TextView) findViewById(R.id.videosTitle);
        final TextView rT = (TextView) findViewById(R.id.reviewsTitle);
        iAPI apiService = retrofit.create(iAPI.class);
        Call<VideosClass> call;
        call = apiService.listVideos(Id, BuildConfig.OPEN_WEATHER_MAP_API_KEY);
        call.enqueue(new Callback<VideosClass>() {
            @Override
            public void onResponse(Call<VideosClass> call, Response<VideosClass> response) {
                if (response.code() == 200) {
                    if (response.body().results == null ) {
                        vT.setVisibility(View.INVISIBLE);
                    }
                    else {
                        list = response.body().results;
                        final VideosAdapter adapter = new VideosAdapter(getBaseContext(),list);
                        videos.setAdapter(adapter);
                        Utility.setListViewHeightBasedOnChildren(videos);
                        videos.setOnItemClickListener(mMessageClickedHandler);
                    }
                }
            }
            @Override
            public void onFailure(Call<VideosClass> call, Throwable t) {
            }
        });

        Call<ReviewsClass> callRev;
        callRev = apiService.listReviews(Id, BuildConfig.OPEN_WEATHER_MAP_API_KEY);
        callRev.enqueue(new Callback<ReviewsClass>() {
            @Override
            public void onResponse(Call<ReviewsClass> callRev, Response<ReviewsClass> response) {
                if (response.code() == 200) {
                    if (response.body().results == null ) {
                        rT.setVisibility(View.INVISIBLE);
                    }
                    else {
                        listRev = response.body().results;
                        final ReviewsAdapter adapter = new ReviewsAdapter(getBaseContext(),listRev);
                        reviews.setAdapter(adapter);
                        Utility.setListViewHeightBasedOnChildren(reviews);
                        reviews.setOnItemClickListener(reviewMessageClickedHandler);
                    }
                }
            }
            @Override
            public void onFailure(Call<ReviewsClass> callRev, Throwable t) {
            }
        });
    }

    private AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + list.get(position).key));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + list.get(position).key));
                startActivity(intent);
            }
        }
    };

    private AdapterView.OnItemClickListener reviewMessageClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(listRev.get(position).url));
            startActivity(intent);
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addToFavorite(View view) {
        Log.w("test mID", mID);
        Log.w("test mReleaseDate", mReleaseDate);
        Log.w("test mVoteAverage", mVoteAverage);
        Log.w("test mOverview", mOverview);
        Log.w("test mPosterPath", mPosterPath);
        Log.w("test title", title);
        Log.w("test backdropPath", backdropPath);

        ContentValues values = new ContentValues();
        values.put(FavoriteTable.COLUMN_MOVIE_ID, mID);
        values.put(FavoriteTable.COLUMN_RELEASE_DATE, mReleaseDate);
        values.put(FavoriteTable.COLUMN_VOTE_AVERAGE, mVoteAverage);
        values.put(FavoriteTable.COLUMN_OVERVIEW, mOverview);
        values.put(FavoriteTable.COLUMN_POSTER_PATH, mPosterPath);
        values.put(FavoriteTable.COLUMN_TITLE, title);
        values.put(FavoriteTable.COLUMN_BACKDROP_PATH, backdropPath);

        if (isFavorite) {
            getContentResolver().delete(FavoriteContentProvider.CONTENT_URI, FavoriteTable.COLUMN_MOVIE_ID  + " = " + mID, null);
            favoiteBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp),null,null,null);
            Snackbar.make(view, R.string.removed_from_favorite, Snackbar.LENGTH_LONG).show();
        } else {
            getContentResolver().insert(FavoriteContentProvider.CONTENT_URI, values);
            favoiteBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_favorite_gold_24dp),null,null,null);
            Snackbar.make(view, R.string.added_to_favorite, Snackbar.LENGTH_LONG).show();
        }


    }
}
