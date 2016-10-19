package com.example.enuviel.googleimagesearch;

import android.util.Log;

import org.json.JSONObject;

/**
 * Created by Enuviel on 10/18/16.
 */
class GoogleSearchImage {

    private static final String TAG = GoogleSearchImage.class.getSimpleName();
    private static final boolean debug = true;

    String link;
    String thumbnailLink;
    String title;
    String contextLink;

    GoogleSearchImage parse(JSONObject json) {
        this.link = json.optString("link");
        this.title = json.optString("htmlTitle");

        try {
            this.thumbnailLink = json.getJSONObject("image").optString("thumbnailLink");
            this.contextLink = json.getJSONObject("image").optString("contextLink");
        } catch (Exception e) {
            Log.e(TAG, "Cannot parse JSON", e);

        }
        return this;
    }

}
