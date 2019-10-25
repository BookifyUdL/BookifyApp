package com.example.readify.Models;

public class Review {

    private String comment;
    private User user;

    public Review(User user, String comment){
        this.user = user;
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
