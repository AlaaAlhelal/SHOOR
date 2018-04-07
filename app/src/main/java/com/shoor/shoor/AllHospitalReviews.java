package com.shoor.shoor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;



public class AllHospitalReviews  extends BaseAdapter {
    Context context;
    ArrayList<Review> AllReviews  ;
    LayoutInflater inflter;

    public AllHospitalReviews(Context applicationContext, ArrayList<Review> AllReviews ) {
        this.context = applicationContext;
        this.AllReviews=AllReviews;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return AllReviews.size();
    }

    @Override
    public Object getItem(int i) {
        return AllReviews.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return AllReviews.size();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.hospitalreview, null);
        TextView names = (TextView)           view.findViewById(R.id.UserNameReview);
        TextView comments = (TextView)           view.findViewById(R.id.UserCommentReview);
        RatingBar rates = (RatingBar) view.findViewById(R.id.UserRatingReview);
        ImageView imageView = (ImageView) view.findViewById(R.id.hos_Pic);
        names.setText(AllReviews.get(i).getUserName());
        comments.setText(AllReviews.get(i).getComment());
        rates.setRating(AllReviews.get(i).getRateScore());
        if(AllReviews.get(i).getPic()!=null)
            imageView.setImageBitmap(getImage(AllReviews.get(i).getPic()));

        return view;
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}
