package com.example.enuviel.googleimagesearch;

import android.net.Uri;
import android.util.Log;

/**
 * Created by Enuviel on 10/18/16.
 */

public class Utils {
    private static final String TAG = Utils.class.getSimpleName();
    private static final boolean debug = true;
    private static final String BASE_URL = "https://www.googleapis.com/customsearch/v1";
    private static final String API_KEY = "AIzaSyAUESxxSfYV1Pe4AzTOUJeXloEBw3JQVs0";
    private static final String API_CX = "005053781825038810546:ty01rv4nrhc";
    private static final String PARAM_KEY = "key";
    private static final String PARAM_Q = "q";
    private static final String PARAM_CX = "cx";
    private static final String PARAM_SEARCHTYPE = "searchType";
    private static final String PARAM_START = "start";
    private static final String PARAM_PARAM_SEARCHTYPE_VALUE = "image";

    /**
     * example of url:
     * https://www.googleapis.com/customsearch/v1?key=AIzaSyBmLKXupx2eSeQXNXx-e1ZMwiL2vlNvhI0&cx=005942433889955329471:6_ji3gzu8i4&q=cats&start=10&searchType=image
     */


    public static String buildUrlList(String searchText, int loadsCount, int limitPerDownload) {
        Uri.Builder uriB = Uri.parse(BASE_URL)
                .buildUpon()
                .appendQueryParameter(PARAM_KEY, API_KEY)
                .appendQueryParameter(PARAM_CX, API_CX)
                .appendQueryParameter(PARAM_Q, searchText)
                .appendQueryParameter(PARAM_SEARCHTYPE, PARAM_PARAM_SEARCHTYPE_VALUE);

        if (loadsCount * limitPerDownload > 0) {
            uriB.appendQueryParameter(PARAM_START, Integer.toString(loadsCount * limitPerDownload));
        }

        if (debug) Log.w(TAG, "url " + uriB.build().toString());

        return uriB.build().toString();
    }

    public static boolean isEmpty(CharSequence str) {
        if (str == null) return true;
        if (str.length() == 0) return true;
        if (str.toString().trim().length() == 0) return true;
        return false;
    }
}
