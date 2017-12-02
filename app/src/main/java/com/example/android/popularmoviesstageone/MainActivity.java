package com.example.android.popularmoviesstageone;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesstageone.adapters.MovieListAdapter;
import com.example.android.popularmoviesstageone.controllers.MovieApiController;
import com.example.android.popularmoviesstageone.controllers.MovieApiInterface;
import com.example.android.popularmoviesstageone.exceptions.NoConnectivityException;
import com.example.android.popularmoviesstageone.models.Movie;
import com.example.android.popularmoviesstageone.models.MovieResponse;
import com.example.android.popularmoviesstageone.utils.BuildConfig;
import com.example.android.popularmoviesstageone.utils.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements
        MovieListAdapter.MovieListAdapterOnClickHandler, SwipeRefreshLayout.OnRefreshListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String STATE_DIALOG = "state_dialog";
    private static final String STATE_MOVIE = "state_movie";


    final Context mContext = this;
    private Toast mToast;
    private List<Movie> mMovieList;
    private MovieResponse mMovieResponse;
    private RecyclerView mRecyclerView;
    private MovieListAdapter mMovieListAdapter;
    private ProgressBar mLoadingIndicator;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Dialog mDialog;
    private TextView mTextViewDialogTitle;
    private TextView mTextViewDialogCaption;
    private Button mButtonDialog;

    private Movie mCurrentMovie;
    private boolean mIsDialogVisible = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        initializeUI();
        setCustomTypeface();

        // Enable layout for SwipeRefresh
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorAccent));

        // Initialize RecyclerView for displaying movie poster images using grid layout
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, setGridColumns());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true); // indicates changes in content don't change child layout size in the RecyclerView

        // Set Adapter
        mMovieListAdapter = new MovieListAdapter(this);
        mRecyclerView.setAdapter(mMovieListAdapter);

        // Load Movie Data
        loadMovieData();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STATE_MOVIE, mCurrentMovie);
        outState.putBoolean(STATE_DIALOG, mIsDialogVisible);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mCurrentMovie = savedInstanceState.getParcelable(STATE_MOVIE);
            mIsDialogVisible = savedInstanceState.getBoolean(STATE_DIALOG);
        }
    }

    /**
     * Method to initialize UI elements
     */
    public void initializeUI() {
        mRecyclerView = findViewById(R.id.recyclerview_movies);
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        mLoadingIndicator = findViewById(R.id.progress_indicator);

        mDialog = new Dialog(mContext);
        mDialog.setContentView(R.layout.dialog);
        mTextViewDialogTitle = mDialog.findViewById(R.id.text_dialog_title);
        mTextViewDialogCaption = mDialog.findViewById(R.id.text_dialog_caption);
        mButtonDialog = mDialog.findViewById(R.id.button_dismiss_dialog);
    }

    /**
     * Method to set custom font for views
     */
    public void setCustomTypeface() {
        Utils.setCustomTypeface(mContext, mTextViewDialogCaption);
        Utils.setCustomTypeface(mContext, mTextViewDialogTitle);
        Utils.setCustomTypeface(mContext, mButtonDialog);
    }

    /**
     * Method to determine number of columns in grid layout depending on orientation
     * @return gridColumns (4 for portrait, 6 for landscape)
     */
    public int setGridColumns() {
        int gridColumns = 0;

        switch (getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                gridColumns = BuildConfig.GRID_COLUMNS_PORTRAIT;
                break;

            case Configuration.ORIENTATION_LANDSCAPE:
                gridColumns = BuildConfig.GRID_COLUMNS_LANDSCAPE;
                break;
        }

        return gridColumns;
    }

    /**
     * Method to load movie data into the adapter and display in the RecyclerView layout
     * Displays alert messages if
     * (1) API key is found missing
     * (2) There is no connectivity
     * (3) A failure happened while fetching/loading movie data from the API
     */
    public void loadMovieData() {

        if (Utils.isEmptyString(BuildConfig.API_KEY)) {
            displayDialog(getString(R.string.alert_api_key_missing));
            mSwipeRefreshLayout.setRefreshing(false);
            return;
        }

        // Get the sort order preference before making a call to the API
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String sortByPref = sharedPrefs.getString(
                getString(R.string.settings_sort_by_key),
                getString(R.string.settings_sort_by_default)
        );

        // Set Activity title
        setActivityTitle(sortByPref);

        // Make Retrofit call to TMDB API
        try {
            mSwipeRefreshLayout.setRefreshing(false);
            mLoadingIndicator.setVisibility(View.VISIBLE);

            MovieApiInterface apiInterface = MovieApiController.
                    getClient(mContext).
                    create(MovieApiInterface.class);

            final Call<MovieResponse> responseCall = apiInterface.getMovieList(sortByPref, BuildConfig.API_KEY);

            responseCall.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    int statusCode = response.code();
                    if (response.isSuccessful()) {
                        mLoadingIndicator.setVisibility(View.INVISIBLE);
                        mMovieResponse = response.body();
                        mMovieListAdapter.setMovieData(mMovieResponse);
                        mMovieListAdapter.notifyDataSetChanged();
                        mMovieList = mMovieResponse.getMovieList();
                    } else {
                        mLoadingIndicator.setVisibility(View.INVISIBLE);
                        displayDialog(getString(R.string.error_movie_load_failed) + statusCode);
                    }
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {
                    mLoadingIndicator.setVisibility(View.INVISIBLE);
                    displayDialog(getString(R.string.error_movie_fetch_failed));
                }
            });
        } catch (NoConnectivityException nce) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            displayDialog(getString(R.string.error_no_connection));
        }
    }

    /**
     * Refreshes movie list when screen is swiped
     */
    @Override
    public void onRefresh() {
        loadMovieData();
    }

    /**
     * This method launches DetailActivity screen when a movie poster is clicked
     * @param movie
     */
    @Override
    public void onClick(Movie movie) {
        mCurrentMovie = movie;
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(BuildConfig.INTENT_EXTRA_KEY_MOVIE, mCurrentMovie);
        intent.putExtra(BuildConfig.INTENT_EXTRA_KEY_TITLE, getTitle());
        startActivity(intent);
    }

     /**
     * Method to set the Activity Title depending on Movie List Preference selected
     * @param pref - sort order preference (e.g. popular/top_rated/upcoming/now_playing)
     */
    public void setActivityTitle(String pref) {
        if (pref.equals(getString(R.string.settings_sort_by_popularity_value))) {
            setTitle(getString(R.string.label_movies, getString(R.string.settings_sort_by_popularity_label)));
        } else if (pref.equals(getString(R.string.settings_sort_by_rating_value))) {
            setTitle(getString(R.string.label_movies, getString(R.string.settings_sort_by_rating_label)));
        } else if (pref.equals(getString(R.string.settings_sort_by_nowplaying_value))) {
            setTitle(getString(R.string.label_movies, getString(R.string.settings_sort_by_nowplaying_label)));
        } else if (pref.equals(getString(R.string.settings_sort_by_upcoming_value))) {
            setTitle(getString(R.string.label_movies, getString(R.string.settings_sort_by_upcoming_label)));
        }
    }

    /**
     * Inflate Settings Menu for Movie List
     * @param menu
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * Method to handle when menu item is clicked
     * @param item
     * @return true
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Method to display custom dialog
     */
    public void displayDialog(String dialogMessage) {
        mTextViewDialogCaption.setText(dialogMessage);

        mButtonDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextViewDialogCaption.setText("");
                mDialog.dismiss();
                mIsDialogVisible = false;
            }
        });
        mDialog.show();
        mIsDialogVisible = true;
    }
}
