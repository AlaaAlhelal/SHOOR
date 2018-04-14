package com.shoor.shoor;


import android.graphics.Bitmap;

public class Review {
    private String userName;
    private String Comment;
    private float RateScore;
    private String Pic;
public Review(String name, String comment, float score){
    userName=name;
     Comment=comment;
    RateScore=score;
}

    public Review(String name, String comment, float score, String pic){
        userName=name;
        Comment=comment;
        RateScore=score;
        Pic =pic;
    }

    public  String getPic() {
        return Pic;
    }

    public void setPic( String pic) {
        Pic = pic;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public void setRateScore(float rateScore) {
        RateScore = rateScore;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public float getRateScore() {
        return RateScore;
    }
}
