package com.example.android.popularmoviesstageone.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

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
}
