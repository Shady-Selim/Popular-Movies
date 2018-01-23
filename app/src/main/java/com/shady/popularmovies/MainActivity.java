package com.shady.popularmovies;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;

import com.shady.popularmovies.contentprovider.FavoriteContentProvider;
import com.shady.popularmovies.database.FavoriteTable;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shady.popularmovies.iAPI.retrofit;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private ProgressBar pb;
    private List<MovieClass> list;
    private MoviesAdapter mAdapter;
    private RadioGroup moviesSelectView;
//    private SwitchCompat switchCompat;
    private int tRated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        moviesSelectView = (RadioGroup) findViewById(R.id.moviesSelectView);
        moviesSelectView.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checked) {
                View radioButton = radioGroup.findViewById(checked);
                loadView(radioGroup.indexOfChild(radioButton));
            }
        });

//        switchCompat = (SwitchCompat) findViewById(R.id.moviesSwitch);

        if (!CheckNetwork.isNetworkAvailable(getBaseContext())) {
            Snackbar.make(findViewById(R.id.cl), getString(R.string.check_internet), Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.close), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            }).show();
        } else {
            if (savedInstanceState != null){
                loadView(savedInstanceState.getInt("switchState"));
                RadioButton checkBox = (RadioButton) moviesSelectView.getChildAt(savedInstanceState.getInt("switchState"));
                checkBox.setChecked(true);
                Log.e("Load from","saved");
            }else {
                RadioButton checkBox = (RadioButton) moviesSelectView.getChildAt(0);
                checkBox.setChecked(true);
                loadView(0);
                Log.e("Load from","fresh");
            }
        }

//        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                loadView(isChecked);
//            }
//        });
    }

    private void loadView(int displayOption) {
        Log.e("selected",displayOption +"");
        tRated = displayOption;
        pb = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        pb.setVisibility(View.VISIBLE);

        final RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        rv.setLayoutManager(layoutManager);

        if (tRated == 2){
            //String URL = "content://com.shady.popularmovies.contentprovider.favorites";
            //Uri m = Uri.parse(URL);
            Cursor c = getContentResolver().query(FavoriteContentProvider.CONTENT_URI, null, null, null, null); //managedQuery

            if (c!=null) {
                list = new ArrayList<>();
                if (c.moveToFirst()){
                    do{
                        MovieClass mc = new MovieClass();
                        mc.id = c.getInt(c.getColumnIndex(FavoriteTable.COLUMN_MOVIE_ID));
                        mc.overview = c.getString(c.getColumnIndex(FavoriteTable.COLUMN_OVERVIEW));
                        mc.releaseDate = c.getString(c.getColumnIndex(FavoriteTable.COLUMN_RELEASE_DATE));
                        mc.title = c.getString(c.getColumnIndex(FavoriteTable.COLUMN_TITLE));
                        mc.voteAverage = c.getDouble(c.getColumnIndex(FavoriteTable.COLUMN_VOTE_AVERAGE));
                        mc.backdropPath = c.getString(c.getColumnIndex(FavoriteTable.COLUMN_BACKDROP_PATH));
                        mc.posterPath = c.getString(c.getColumnIndex(FavoriteTable.COLUMN_POSTER_PATH));
                        list.add(mc);
                    } while (c.moveToNext());
                }
                c.close();
                pb.setVisibility(View.INVISIBLE);
            }

            Log.e("list size",list.size() +"");
            mAdapter = new MoviesAdapter(getBaseContext(), list);
            rv.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(onItemClickListener);
        }
        else{
            iAPI apiService = retrofit.create(iAPI.class);
            Call<MoviesClass> call;
            call = (tRated != 0) ? apiService.listTopRated(BuildConfig.OPEN_WEATHER_MAP_API_KEY) : apiService.listPopular(BuildConfig.OPEN_WEATHER_MAP_API_KEY);
            call.enqueue(new Callback<MoviesClass>() {
                @Override
                public void onResponse(Call<MoviesClass> call, Response<MoviesClass> response) {
                    pb.setVisibility(View.INVISIBLE);

                    if (response.code() == 200) {
                        list = response.body().results;
                        mAdapter = new MoviesAdapter(getBaseContext(), list);
                        rv.setAdapter(mAdapter);
                        mAdapter.setOnItemClickListener(onItemClickListener);
                    }
                }

                @Override
                public void onFailure(Call<MoviesClass> call, Throwable t) {
                    pb.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    MoviesAdapter.OnItemClickListener onItemClickListener = new MoviesAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, int position) {
            Intent intent = new Intent(getBaseContext(), ScrollingActivity.class);
            intent.putExtra("id", list.get(position).id);
            intent.putExtra("title", list.get(position).title);
            intent.putExtra("releaseDate", list.get(position).releaseDate);
            intent.putExtra("posterPath", list.get(position).posterPath);
            intent.putExtra("backdropPath", list.get(position).backdropPath);
            intent.putExtra("voteAverage", list.get(position).voteAverage.toString());
            intent.putExtra("overview", list.get(position).overview);
            startActivity(intent);
        }
    };

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("switchState", tRated);
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = { FavoriteTable.COLUMN_ID, FavoriteTable.COLUMN_BACKDROP_PATH, FavoriteTable.COLUMN_MOVIE_ID, FavoriteTable.COLUMN_OVERVIEW, FavoriteTable.COLUMN_POSTER_PATH, FavoriteTable.COLUMN_RELEASE_DATE, FavoriteTable.COLUMN_TITLE, FavoriteTable.COLUMN_VOTE_AVERAGE };
        CursorLoader cursorLoader = new CursorLoader(this, FavoriteContentProvider.CONTENT_URI, projection, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        //mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //mAdapter.swapCursor(null);
    }
}
