package com.example.readify.Models;

import android.net.Uri;

import com.example.readify.CommentType;

import java.net.URI;

public class Review {

    private String comment;
    private User user;
    private CommentType commentType;
    private Uri uri;
    private int likes;

    public Review(User user, String comment){
        this.user = user;
        this.comment = comment;
        this.commentType = CommentType.COMMENT;
        this.likes = 0;
    }

    public Review(User user, String comment, CommentType commentType, Uri uri){
        this.user = user;
        this.comment = comment;
        this.commentType = commentType;
        this.uri = uri;
        this.likes = 0;
    }

    public int getLikes(){
        return this.likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri){
        this.uri = uri;
    }

    public CommentType getCommentType(){
        return  this.commentType;
    }

    public void setCommentType(CommentType commentType){
        this.commentType = commentType;
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
