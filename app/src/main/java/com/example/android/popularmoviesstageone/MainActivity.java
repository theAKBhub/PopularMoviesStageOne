package com.example.android.popularmoviesstageone;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesstageone.adapters.MovieListAdapter;
import com.example.android.popularmoviesstageone.controllers.MovieApiController;
import com.example.android.popularmoviesstageone.controllers.MovieApiInterface;
import com.example.android.popularmoviesstageone.exceptions.NoConnectivityException;
import com.example.android.popularmoviesstageone.models.Movie;
import com.example.android.popularmoviesstageone.models.MovieResponse;
import com.example.android.popularmoviesstageone.utils.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MovieListAdapter.MovieListAdapterOnClickHandler {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    // TODO - add TMDb API Key
    private static final String API_KEY = "2220f4fecad926324cab32a5df860aee";

    private static final int GRID_VIEW_COLUMNS = 2;

    final Context mContext = this;
    private Toast mToast;
    private List<Movie> mMovieList;
    private MovieResponse mMovieResponse;
    private RecyclerView mRecyclerView;
    private MovieListAdapter mMovieListAdapter;
    private TextView mTextViewErrorMessage;
    private ProgressBar mLoadingIndicator;
    GridLayoutManager mGridLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);
        mTextViewErrorMessage = (TextView) findViewById(R.id.text_movie_load_error);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, GRID_VIEW_COLUMNS);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true); // indicates changes in content don't change child layout size in the RecyclerView

        mMovieListAdapter = new MovieListAdapter(this);
        mRecyclerView.setAdapter(mMovieListAdapter);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.progress_indicator);
        loadMovieData();



    }


    public void loadMovieData() {

        //showDataView();

        if (Utils.isEmptyString(API_KEY)) {
            mToast = Utils.showToastMessage(mContext, mToast, getString(R.string.alert_api_key_missing));
            mToast.show();
            return;
        }

        try {
            MovieApiInterface apiInterface = MovieApiController.
                    getClient(mContext).
                    create(MovieApiInterface.class);

            final Call<MovieResponse> responseCall = apiInterface.getMovieList(API_KEY);

            responseCall.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    int statusCode = response.code();
                    if (response.isSuccessful()) {
                        //mMovieList = response.body().getMovieList();
                        mMovieResponse = response.body();
                        mMovieListAdapter.setMovieData(mMovieResponse);
                        mMovieListAdapter.notifyDataSetChanged();

                        mMovieList = mMovieResponse.getMovieList();
                        Log.d(LOG_TAG, "Number of movies received: " + mMovieList.size());
                        for (Movie mov : mMovieList) {
                            Log.d(LOG_TAG, mov.getTitle() + "/" + mov.getReleaseDate() + "/" + mov.getId() + "/" + mov.getPosterPath());
                        }
                    } else {
                        mToast = Utils.showToastMessage(mContext, mToast, getString(R.string.error_movie_load_failed) + statusCode);
                        mToast.show();
                    }
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {
                    mToast = Utils.showToastMessage(mContext, mToast, getString(R.string.error_movie_fetch_failed));
                    mToast.show();
                }
            });
        } catch (NoConnectivityException nce) {
            mToast = Utils.showToastMessage(mContext, mToast, getString(R.string.error_no_connection));
            mToast.show();
        }
    }

    public void showDataView() {
        mTextViewErrorMessage.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(Movie movie) {
        mToast = Utils.showToastMessage(mContext, mToast, movie.getTitle() + "/" + movie.getPosterPath());
        mToast.show();
    }
}
