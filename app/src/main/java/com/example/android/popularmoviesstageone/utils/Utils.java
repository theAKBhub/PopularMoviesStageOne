package com.example.android.popularmoviesstageone.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesstageone.R;

import java.util.Locale;

/**
 * This class contains common methods
 * Created by aditibhattacharya on 24/11/2017.
 */

public class Utils {

    public static final String LOG_TAG = Utils.class.getSimpleName();

    /**
     * This is a private constructor and only meant to hold static variables and methods,
     * which can be accessed directly from the class name Utils
     */
    private void Utils() {
    }


    /**
     * Utility method to check if a string is empty or not
     * @param stringToCheck
     * @return TRUE (if empty string) / FALSE
     */
    public static boolean isEmptyString(String stringToCheck) {
        return (stringToCheck == null || stringToCheck.trim().length() == 0);
    }


    /**
     * Utility method to check if device has got connectivity needed to access TMDb API
     * @param context
     * @return TRUE (if connected) / FALSE
     */
    public static boolean hasConnectivity(Context context) {
        final ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }


    /**
     * Utility method to construct a Toast message
     * @param context
     * @param toast
     * @param message
     * @return Toast object
     */
    public static Toast showToastMessage(Context context, Toast toast, String message) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        return toast;
    }


    /**
     * Method to set custom typeface to UI elements
     */
    public static void setCustomTypeface(Context context, View view) {
        Typeface typefaceRegular = Typeface.createFromAsset(context.getAssets(),
                "fonts/roboto_condensed_r.ttf");
        Typeface typefaceThin = Typeface.createFromAsset(context.getAssets(),
                "fonts/roboto_condensed_l.ttf");
        Typeface typefaceRegularNoto = Typeface.createFromAsset(context.getAssets(),
                "fonts/notosans_r.ttf");

        // Get tag on the view
        String viewTag = view.getTag().toString();

        // Cast view to appropriate view element based on tag received and set typefaces
        if (viewTag.equals(context.getString(R.string.tag_regular_roboto_cond))) {
            TextView textView = (TextView) view;
            textView.setTypeface(typefaceRegular);
        } else if (viewTag.equals(context.getString(R.string.tag_light_roboto_cond))) {
            TextView textView = (TextView) view;
            textView.setTypeface(typefaceThin);
        } else if (viewTag.equals(context.getString(R.string.tag_regular_noto))) {
            TextView textView = (TextView) view;
            textView.setTypeface(typefaceRegularNoto);
        }
    }

    /**
     * Method to convert ISO language codes to language names (e.g. en -> English, es -> Español)
     * @param languageCode
     * @return language name
     */
    public static String getLanguageName(String languageCode) {
        String language;

        Locale locale = new Locale(languageCode);
        language = locale.getDisplayLanguage(locale);
        language = language.substring(0, 1).toUpperCase() + language.substring(1);

        return language;
    }

}
