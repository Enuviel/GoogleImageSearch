package com.example.enuviel.googleimagesearch;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Enuviel on 10/18/16.
 */

public class GoogleSearchAdapter extends RecyclerView.Adapter<GoogleSearchAdapter.ViewHolder> {

    private static final String TAG = GoogleSearchAdapter.class.getSimpleName();
    private static final boolean debug = true;

    private List<GoogleSearchImage> imagesList;
    private Activity mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageView1;
        private ImageView mImageView2;
        private ImageView mImageView3;

        private ViewHolder(View v) {
            super(v);
            mImageView1 = (ImageView) v.findViewById(R.id.image1);
            mImageView2 = (ImageView) v.findViewById(R.id.image2);
            mImageView3 = (ImageView) v.findViewById(R.id.image3);
        }
    }


    public GoogleSearchAdapter(List<GoogleSearchImage> imageList, Activity context) {
        this.imagesList = imageList;
        mContext = context;
    }

    @Override
    public GoogleSearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (debug) Log.w(TAG, "onCreateViewHolder() viewType: " + viewType);
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_convert, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (debug) Log.w(TAG, "onBindViewHolder() pos: " + position + ", holder: " + holder);
        final GoogleSearchImage image1 = imagesList.get(position * 3);
        final GoogleSearchImage image2 = imagesList.get(position * 3 + 1);
        final GoogleSearchImage image3 = imagesList.get(position * 3 + 2);

        if (!TextUtils.isEmpty(image1.thumbnailLink)) {

            Glide.with(mContext)
                    .load(imagesList.get(position * 3).thumbnailLink)
                    .centerCrop()
                    .crossFade()
                    .into(holder.mImageView1);
        } else {
            holder.mImageView1.setImageDrawable(null);
        }
        if (!TextUtils.isEmpty(image2.thumbnailLink)) {

            Glide.with(mContext)
                    .load(imagesList.get(position * 3 + 1).thumbnailLink)
                    .centerCrop()
                    .crossFade()
                    .into(holder.mImageView2);
        } else {
            holder.mImageView2.setImageDrawable(null);
        }
        if (!TextUtils.isEmpty(image3.thumbnailLink)) {

            Glide.with(mContext)
                    .load(imagesList.get(position * 3 + 2).thumbnailLink)
                    .centerCrop()
                    .crossFade()
                    .into(holder.mImageView3);
        } else {
            holder.mImageView3.setImageDrawable(null);
        }

    }

    @Override
    public int getItemCount() {
        int result = imagesList.size() / 3;
        if (debug) Log.w(TAG, "onItemCount() items: " + imagesList.size() + ", rows: " + result);
        return result;
    }
}


