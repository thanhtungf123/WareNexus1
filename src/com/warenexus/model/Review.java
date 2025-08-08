package com.warenexus.model;

import java.util.Date;

public class Review {
    private int reviewID;
    private int rentalOrderID;
    private int rating;
    private String comment;
    private Date reviewDate;

    public int getReviewID() {
        return reviewID;
    }

    public void setReviewID(int reviewID) {
        this.reviewID = reviewID;
    }

    public int getRentalOrderID() {
        return rentalOrderID;
    }

    public void setRentalOrderID(int rentalOrderID) {
        this.rentalOrderID = rentalOrderID;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }
}
