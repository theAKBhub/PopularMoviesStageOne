package com.example.android.popularmoviesstageone.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * A {@link MovieResponse} object that contains a list of Movie Items
 * Created by aditibhattacharya on 23/11/2017.
 */

public class MovieResponse {

    /**
     * {@link MovieResponse} Attributes
     * Each attribute has a corresponding @SerializedName that is needed for GSON
     * to map the JSON keys (from TMDB API) with the attributes of {@link MovieResponse} object.
     */

    // Movie List
    @SerializedName("results")
    private List<Movie> mMovieList;

    // Page Number
    @SerializedName("page")
    private int mPage;

    // Total number of movies in the list
    @SerializedName("total_results")
    private int mTotalCount;

    // Total number of pages
    @SerializedName("total_pages")
    private int mTotalPages;


    /**
     * Empty constructor
     */
    public MovieResponse() {

    }

    /**
     * Default Constructor - Constructs a new {@link MovieResponse} object
     * @param movieList
     * @param page
     * @param totalCount
     * @param totalPages
     */
    private MovieResponse(List<Movie> movieList, int page, int totalCount, int totalPages) {
        mMovieList = movieList;
        mPage = page;
        mTotalCount = totalCount;
        mTotalPages = totalPages;
    }

    /** Getter method - Movie List */
    public List<Movie> getMovieList() {
        return mMovieList;
    }

    /** Setter method - Movie List */
    public void setMovieList(List<Movie> movieList) {
        mMovieList = movieList;
    }

    /** Getter method - Page */
    public int getPage() {
        return mPage;
    }

    /** Setter method - Page */
    public void setPage(int page) {
        mPage = page;
    }

    /** Getter method - Total Count */
    public int getTotalCount() {
        return mTotalCount;
    }

    /** Setter method - Total Count */
    public void setTotalCount(int totalCount) {
        mTotalCount = totalCount;
    }

    /** Getter method - Total Pages */
    public int getTotalPages() {
        return mTotalPages;
    }

    /** Setter method - Total Pages */
    public void setTotalPages(int totalPages) {
        mTotalPages = totalPages;
    }
}
