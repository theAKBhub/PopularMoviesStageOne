package com.example.android.popularmoviesstageone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmoviesstageone.models.Movie;
import com.example.android.popularmoviesstageone.utils.BuildConfig;
import com.example.android.popularmoviesstageone.utils.Utils;
import com.squareup.picasso.Picasso;


public class DetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    final Context mContext = this;

    private Movie mCurrentMovie;
    private ImageView mImageViewMoviePoster;
    private Animation mShowAnimation;

    private TextView mTextViewMovieTitle;
    private TextView mTextViewMovieDate;
    private TextView mTextViewMovieLanguage;
    private TextView mTextViewMovieGenre;
    private TextView mTextViewMovieVote;
    private TextView mTextViewMovieOverview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Retrieve Intent Extras
        Intent intent = getIntent();
        if (intent.getParcelableExtra(BuildConfig.INTENT_EXTRA_KEY_MOVIE) != null) {
            mCurrentMovie = intent.getParcelableExtra(BuildConfig.INTENT_EXTRA_KEY_MOVIE);
        }
        if (intent.getExtras() != null) {
            setTitle(intent.getStringExtra(BuildConfig.INTENT_EXTRA_KEY_TITLE));
        }

        // Initialize views
        initializeUI();
        setCustomFont((ViewGroup)this.findViewById(android.R.id.content));

        // Display movie data
        displayMovieData();
    }

    /**
     * Method to initialize UI elements
     */
    public void initializeUI() {
        mImageViewMoviePoster = findViewById(R.id.image_movie);
        mTextViewMovieTitle = findViewById(R.id.text_movie_title);
        mTextViewMovieDate = findViewById(R.id.text_movie_date);
        mTextViewMovieLanguage = findViewById(R.id.text_movie_language);
        mTextViewMovieGenre = findViewById(R.id.text_movie_genre);
        mTextViewMovieVote = findViewById(R.id.text_movie_vote);
        mTextViewMovieOverview = findViewById(R.id.text_movie_overview);
        mShowAnimation = AnimationUtils.loadAnimation(mContext, R.anim.poster_anim);
    }

    /**
     * Method to set custom font for views
     */
    public void setCustomFont(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof TextView) {
                Utils.setCustomTypeface(mContext, view);
            } else if (view instanceof ViewGroup) {
                setCustomFont((ViewGroup) view);
            }
        }
    }

    /**
     * Method that controls display of movie data
     */
    public void displayMovieData() {
        int [] genreIds = mCurrentMovie.getGenreIds();

        // Display Backdrop Poster Image
        Picasso.with(mContext)
                .load(BuildConfig.BACKDROP_POSTER_BASE_URL + mCurrentMovie.getBackdropPath())
                .into(mImageViewMoviePoster);
        mImageViewMoviePoster.startAnimation(mShowAnimation);

        // Display Movie Title
        mTextViewMovieTitle.setText(mCurrentMovie.getTitle());

        // Display Movie Release Date
        mTextViewMovieDate.setText(mCurrentMovie.getReleaseDate());

        // Display Movie Language
        mTextViewMovieLanguage.setText(Utils.getLanguageName(mCurrentMovie.getLanguage()));

        // Display Movie Genre
        mTextViewMovieGenre.setText(mCurrentMovie.getGenres(genreIds));

        // Display Average Vote
        mTextViewMovieVote.setText(getString(R.string.label_vote_display, mCurrentMovie.getVote()));

        // Display Plot Synopsis
        mTextViewMovieOverview.setText(mCurrentMovie.getOverview());
    }
}
