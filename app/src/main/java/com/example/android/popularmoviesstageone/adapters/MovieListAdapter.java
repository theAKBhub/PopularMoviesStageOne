package com.example.android.popularmoviesstageone.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmoviesstageone.R;
import com.example.android.popularmoviesstageone.models.Movie;
import com.example.android.popularmoviesstageone.models.MovieResponse;
import com.example.android.popularmoviesstageone.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * {@link MovieListAdapter} creates a list of weather forecasts to a
 * {@link android.support.v7.widget.RecyclerView}
 *
 * Created by aditibhattacharya on 26/11/2017.
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieListAdapterViewHolder> {

    private final static String MOVIE_POSTER_BASE_URL = "http://image.tmdb.org/t/p/w185/";
    private MovieResponse mMovieResponse;
    private List<Movie> mMovieList;
    private MovieListAdapterOnClickHandler mClickHandler;
    private Context mContext;

    /**
     * Interface to receive onClick messages
     */
    public interface MovieListAdapterOnClickHandler {
        void onClick(Movie movie);
    }

    /**
     * OnClick handler for the adapter that handles situation when a single item is clicked
     * @param clickHandler
     */
    public MovieListAdapter(MovieListAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class MovieListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView mImageViewPoster;

        public MovieListAdapterViewHolder(View view) {
            super(view);
            mImageViewPoster = (ImageView) view.findViewById(R.id.image_movie_poster);
            view.setOnClickListener(this);
        }

        /**
         * This method gets called when child view is clicked
         * @param view
         */
        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Movie movie = mMovieResponse.getMovieList().get(adapterPosition);
            mClickHandler.onClick(movie);
        }
    }


    /**
     * Method called when a new ViewHolder gets created in the event of RecyclerView being laid out.
     * This creates enough ViewHolders to fill up the screen and allow scrolling
     * @param viewGroup
     * @param viewType
     * @return A new MovieListAdapterViewHolder that holds the View for each list item
     */
    @Override
    public MovieListAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        mContext = viewGroup.getContext();
        int listItemLayoutId = R.layout.movie_list_item;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = layoutInflater.inflate(listItemLayoutId, viewGroup, shouldAttachToParentImmediately);
        return new MovieListAdapterViewHolder(view);
    }

    /**
     * Method used by RecyclerView to display the movie poster image
     * @param movieListAdapterViewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(MovieListAdapterViewHolder movieListAdapterViewHolder, int position) {
        String moviePoster = "";
        String moviePosterUrl = "";

        /*if (getItemCount() <= 0 && position >= getItemCount()) {
            return;
        }*/

        if (position < getItemCount()) {
            Movie movie = mMovieResponse.getMovieList().get(position);
            moviePoster = movie.getPosterPath();
            if (!Utils.isEmptyString(moviePoster)) {
                //movieListAdapterViewHolder.mImageViewPoster.setImageResource(R.mipmap.ic_launcher);
                moviePosterUrl = MOVIE_POSTER_BASE_URL + moviePoster;
                Log.d("XXX", moviePosterUrl);
                Picasso.with(mContext).load(moviePosterUrl).into(movieListAdapterViewHolder.mImageViewPoster);
            }
        }
    }

    /**
     * Returns number of items in the fetched movie list
     * @return number of movie items
     */
    @Override
    public int getItemCount() {
        return (mMovieResponse == null) ? 0 : mMovieResponse.getTotalCount();
    }

    /**
     * Method used to refresh the movie list once the MovieListAdapter is
     * already created, to avoid creating a new MovieListAdapter
     * @param movieResponse - the new movie set to be displayed
     */
    public void setMovieData(MovieResponse movieResponse) {
        mMovieResponse = movieResponse;
        mMovieList = mMovieResponse.getMovieList();
        notifyDataSetChanged();
    }
}
