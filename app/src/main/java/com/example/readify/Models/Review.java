package com.example.readify.Models;

import android.net.Uri;

import com.example.readify.CommentType;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;

public class Review {

    public static String COMMENT_PARAMETER = "COMMENT_PARAMETER";
    public static String COMMENT_TYPE_PARAMETER = "COMMENT_TYPE_PARAMETER";
    public static String POSITION_PARAMETER = "POSITION_PARMAETER";
    public static String URI_PARAMETER = "URI";

    private String comment;
    private User user;
    private CommentType commentType;
    private Uri uri;
    private ArrayList<Review> subReviews;
    private ArrayList<User> userLiked;
    private boolean isSub = false;

    public Review(User user, String comment){
        this.user = user;
        this.comment = comment;
        this.commentType = CommentType.COMMENT;
        this.userLiked = new ArrayList<>();
        this.subReviews = new ArrayList<>();
    }

    public Review(User user, String comment, ArrayList<Review> subReviews){
        this.user = user;
        this.comment = comment;
        this.commentType = CommentType.COMMENT;
        this.userLiked = new ArrayList<>();
        this.subReviews = subReviews;
    }

    public Review(User user, String comment, CommentType commentType, Uri uri){
        this.user = user;
        this.comment = comment;
        this.commentType = commentType;
        this.uri = uri;
        //this.likes = 0;
        this.userLiked = new ArrayList<>();
        this.subReviews = new ArrayList<>();

    }

    public void setIsSub(boolean isSub){
        this.isSub = isSub;
    }

    public JSONObject toJsonObject(){
        int type = commentTypeToInt();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user", user.getUid());
            jsonObject.put("message", comment);
            jsonObject.put("uri", uri);
            jsonObject.put("comment_type", type);
            jsonObject.put("is_sub", isSub);
            JSONArray liked = new JSONArray();
            for (User lik : userLiked){
                liked.put(lik.getUid());
            }
            jsonObject.put("user_liked", liked);

        } catch (Exception e) {

        }
        return jsonObject;
    }

    public int commentTypeToInt(){
        int type = 1;
        switch (commentType){
            case COMMENT:
                type = 1;
                break;
            case COMMENT_AND_IMAGE:
                type = 2;
                break;
            case COMMENT_AND_GIF:
                type = 3;
                break;
            default:
                break;
        }
        return type;
    }

    public ArrayList<Review> getSubReviews() {
        return subReviews;
    }

    public void setSubReviews(ArrayList<Review> subReviews) {
        this.subReviews = subReviews;
    }

    public void addSubReview(Review review){
        this.subReviews.add(review);
    }

    public int getLikes(){
        return this.userLiked.size();
    }

    public boolean likedComment(User user){
        return this.userLiked.contains(user);
    }

    public void addLike(User user){
        userLiked.add(user);
    }

    public void removeLike(User user){
        userLiked.remove(user);
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
