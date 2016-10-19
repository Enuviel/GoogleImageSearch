package com.example.enuviel.googleimagesearch;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final Boolean debug = true;

    public static final int COUNT_PER_LOAD = 10;

    private GoogleSearchAdapter mGoogleSearchAdapter;
    private List<GoogleSearchImage> mListOfImages = new ArrayList<>();
    private Button mBtnSearch;
    private RecyclerView mRVImages;
    private String mCurrentSearch;
    private int mCurrentloadsCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText etSearch = (EditText) findViewById(R.id.activity_main_edit_text_search);
        mBtnSearch = (Button) findViewById(R.id.activity_main_button_search);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etSearch.getText().toString().trim().length() == 0) {
                    mBtnSearch.setEnabled(false);
                } else {
                    mBtnSearch.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mGoogleSearchAdapter = new GoogleSearchAdapter(mListOfImages, MainActivity.this);
        final LinearLayoutManager llManager = new LinearLayoutManager(this);
        mRVImages = (RecyclerView) findViewById(R.id.recyclerView);
        mRVImages.setLayoutManager(llManager);
        mRVImages.setAdapter(mGoogleSearchAdapter);

        mRVImages.addOnScrollListener(new EndlessRecyclerViewScrollListener(llManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (debug) Log.w(TAG, "loadData calling from onScroll");
                loadData(mCurrentloadsCount);
            }
        });

        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchForImages(etSearch);
            }
        });
    }

    public void searchForImages(EditText seaEditText) {
        if (Utils.isEmpty(seaEditText.getText())) return;
        if (debug) Log.w(TAG, "onClick() .search: ");
        if (!seaEditText.getText().toString().equals(mCurrentSearch)) {
            mCurrentSearch = seaEditText.getText().toString();
            if (debug) Log.w(TAG, "loadData calling from Search button click");
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(seaEditText.getWindowToken(), 0);
            if (mListOfImages.size() > 0) {
                mListOfImages.clear();
                mRVImages.setItemViewCacheSize(0);
                mCurrentloadsCount = 0;
                if (debug) Log.w(TAG, "clear mListOfImages");
            }
            loadData(0);
        } else {
            Toast.makeText(MainActivity.this, "Please enter new string for search", Toast.LENGTH_SHORT).show();
        }
    }

    public void loadData(final int loadsCount) {
        OkHttpClient okHttp = new OkHttpClient.Builder().build();
        okHttp.newCall(new Request.Builder().url(Utils.buildUrlList(mCurrentSearch, loadsCount, COUNT_PER_LOAD)).build()).enqueue(new Callback() {
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    if (debug) Log.w(TAG, "failed to retrieve");
                    return;
                }
                final JSONObject allJSON;
                try {
                    allJSON = new JSONObject(response.body().string());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (allJSON.has("items")) {
                                    JSONArray itemsArray = allJSON.getJSONArray("items");
                                    if (debug)
                                        Log.i(TAG, "loadData() images before: " + mListOfImages.size());
                                    if (debug)
                                        Log.i(TAG, "loadData() itemsArray:    " + (itemsArray != null ? itemsArray.length() : "null"));
                                    if (itemsArray != null) {
                                        for (int i = 0; i < itemsArray.length(); i++) {
                                            GoogleSearchImage image = new GoogleSearchImage();
                                            image.parse(itemsArray.getJSONObject(i));
                                            mListOfImages.add(image);
                                            mGoogleSearchAdapter.notifyItemRangeChanged(0, mListOfImages.size());
                                        }
                                    }
                                    mCurrentloadsCount++;
                                    if (debug)
                                        Log.i(TAG, "loadData() images after:  " + mListOfImages.size());
                                }

                            } catch (Exception e) {
                                Log.e(TAG, "Cannot parse JSON", e);
                            }

                        }
                    });

                } catch (Exception e) {
                    Log.e(TAG, "Cannot parse JSON", e);
                }
            }

            public void onFailure(Call call, IOException e) {
                if (debug) Log.w(TAG, "failed to retrieve", e);
            }
        });
    }
}
