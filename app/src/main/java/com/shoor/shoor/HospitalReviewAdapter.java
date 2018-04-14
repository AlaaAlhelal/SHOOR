package com.shoor.shoor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;


public class HospitalReviewAdapter  extends RecyclerView.Adapter<HospitalReviewAdapter.ViewHolder> {

    ArrayList<Review> reviews;
    Context context;

    public HospitalReviewAdapter(Context context, ArrayList<Review> data) {
        super();
        this.context = context;
        this.reviews =data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.hospitalreview, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.UserName.setText(reviews.get(i).getUserName());
        viewHolder.Comment.setText(reviews.get(i).getComment());
        viewHolder.RateScore.setRating(reviews.get(i).getRateScore());
        if(reviews.get(i).getPic()!=null) {
            Bitmap bitmap = BitmapFactory.decodeFile(reviews.get(i).getPic());
            viewHolder.image.setImageBitmap(bitmap);
        }

    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder   {

        public TextView UserName;
        public TextView Comment;
        public RatingBar RateScore;
        public ImageView image;
        public ViewHolder(View itemView) {
            super(itemView);
            UserName = (TextView) itemView.findViewById(R.id.UserNameReview);
            Comment = (TextView) itemView.findViewById(R.id.UserCommentReview);
            RateScore =(RatingBar) itemView.findViewById(R.id.UserRatingReview);
            image = (ImageView) itemView.findViewById(R.id.hos_Pic);
        }
    }


    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

}
