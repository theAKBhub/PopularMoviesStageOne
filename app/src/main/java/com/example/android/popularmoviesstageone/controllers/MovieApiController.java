package com.example.android.popularmoviesstageone.controllers;

import android.content.Context;

import com.example.android.popularmoviesstageone.exceptions.NoConnectivityException;
import com.example.android.popularmoviesstageone.utils.BuildConfig;
import com.example.android.popularmoviesstageone.utils.Utils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The purpose of the class is used to issue network requests to TMDb API,
 * using the base URL provided
 *
 * Created by aditibhattacharya on 24/11/2017.
 */

public class MovieApiController {

    private static Retrofit mRetrofit = null;

    public static Retrofit getClient(Context context) throws NoConnectivityException {

        // Check if device has connection, else throw exception error and exit early
        if (!Utils.hasConnectivity(context)) {
            throw new NoConnectivityException();
        }

        if (mRetrofit == null) {
            //Create OkhttpClient.Builder object
            OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

            // Create HttpLoggingInterceptor object and set logging level
            HttpLoggingInterceptor httpLoggingInterceptor =  new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);  // BASIC prints request methods and response codes

            // Couple OkhttpClient.Builder object with HttpLoggingInterceptor object,
            // and set connection timeout duration
            okHttpClientBuilder.addInterceptor(httpLoggingInterceptor);
            okHttpClientBuilder.connectTimeout(BuildConfig.DURATION_CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS);

            // Create Retrofit object and attach OkHttp client to it
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.TMDB_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClientBuilder.build())
                    .build();
        }
        return mRetrofit;
    }
}
