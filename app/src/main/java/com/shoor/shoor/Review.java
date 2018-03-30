package com.shoor.shoor;


public class Review {
    private String userName;
    private String Comment;
    private float RateScore;
public Review(String name, String comment, float score){
    userName=name;
     Comment=comment;
    RateScore=score;
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
