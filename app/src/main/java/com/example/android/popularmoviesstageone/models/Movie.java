package com.example.android.popularmoviesstageone.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * A {@link Movie} object that contains details related to a single Movie item
 * Created by aditibhattacharya on 23/11/2017.
 */

public class Movie implements Parcelable {

    /**
     * {@link Movie} Attributes
     * Each attribute has a corresponding @SerializedName that is needed for GSON
     * to map the JSON keys (from TMDB API) with the attributes of {@link Movie} object.
     */

    // Movie ID
    @SerializedName("id")
    private int mMovieId;

    // Movie Title
    @SerializedName("title")
    private String mMovieTitle;

    // Movie Release Date
    @SerializedName("release_date")
    private String mMovieReleaseDate;

    // Movie Original Language
    @SerializedName("original_language")
    private String mMovieOriginalLanguage;

    // Movie Vote Average
    @SerializedName("vote_average")
    private Double mMovieVoteAverage;

    // Movie Overview
    @SerializedName("overview")
    private String mMovieOverview;

    // Movie Poster Path
    @SerializedName("poster_path")
    private String mMoviePosterPath;

    // Movie Backdrop Path
    @SerializedName("backdrop_path")
    private String mMovieBackdropPath;


    /**
     * Empty constructor
     */
    public Movie() {

    }

    /**
     * Default Constructor - Constructs a new {@link Movie} object
     * Scope for this constructor is private so CREATOR can access it
     * @param parcel
     */
    private Movie(Parcel parcel) {
        mMovieId = parcel.readInt();
        mMovieTitle = parcel.readString();
        mMovieReleaseDate = parcel.readString();
        mMovieOriginalLanguage = parcel.readString();
        mMovieVoteAverage = parcel.readDouble();
        mMovieOverview = parcel.readString();
        mMoviePosterPath = parcel.readString();
        mMovieBackdropPath = parcel.readString();
    }

    /** Getter method - Movie ID */
    public int getId() {
        return mMovieId;
    }

    /** Setter method - Movie ID */
    public void setId(int movieId) {
        mMovieId = movieId;
    }

    /** Getter method - Movie Title */
    public String getTitle() {
        return mMovieTitle;
    }

    /** Setter method - Movie Title */
    public void setTitle(String movieTitle) {
        mMovieTitle = movieTitle;
    }

    /** Getter method - Movie Release Date */
    public String getReleaseDate() {
        return mMovieReleaseDate;
    }

    /** Setter method - Movie Release Date */
    public void setReleaseDate(String movieReleaseDate) {
        mMovieReleaseDate = movieReleaseDate;
    }

    /** Getter method - Movie Language */
    public String getLanguage() {
        return mMovieOriginalLanguage;
    }

    /** Setter method - Movie Language */
    public void setLanguage(String movieOriginalLanguage) {
        mMovieOriginalLanguage = movieOriginalLanguage;
    }

    /** Getter method - Movie Vote */
    public Double getVote() {
        return mMovieVoteAverage;
    }

    /** Setter method - Movie Vote */
    public void setVote(Double movieVoteAvergae) {
        mMovieVoteAverage = movieVoteAvergae;
    }

    /** Getter method - Movie Overview */
    public String getOverview() {
        return mMovieOverview;
    }

    /** Setter method - Movie Overview */
    public void setOverview(String movieOverview) {
        mMovieOverview = movieOverview;
    }

    /** Getter method - Movie Poster Path */
    public String getPosterPath() {
        return mMoviePosterPath;
    }

    /** Setter method - Movie Poster Path */
    public void setPosterPath(String moviePosterPath) {
        mMoviePosterPath = moviePosterPath;
    }

    /** Getter method - Movie Backdrop Path */
    public String getBackdropPath() {
        return mMovieBackdropPath;
    }

    /** Setter method - Movie Backdrop Path */
    public void setBackdropPath(String movieBackdropPath) {
        mMovieBackdropPath = movieBackdropPath;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mMovieId);
        dest.writeString(mMovieTitle);
        dest.writeString(mMovieReleaseDate);
        dest.writeString(mMovieOriginalLanguage);
        dest.writeDouble(mMovieVoteAverage);
        dest.writeString(mMovieOverview);
        dest.writeString(mMoviePosterPath);
        dest.writeString(mMovieBackdropPath);
    }

    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {

        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

}
