package com.example.readify.Models;

import android.net.Uri;

import com.example.readify.CommentType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.Random;

public class Review implements Serializable {

    public static String COMMENT_PARAMETER = "COMMENT_PARAMETER";
    public static String COMMENT_TYPE_PARAMETER = "COMMENT_TYPE_PARAMETER";
    public static String POSITION_PARAMETER = "POSITION_PARMAETER";
    public static String URI_PARAMETER = "URI";

    private String comment;
    private User user;
    public String userId;
    private CommentType commentType;
    private Uri uri;
    private ArrayList<Review> subReviews;
    private ArrayList<String> subReviewsId;
    private ArrayList<User> userLiked;
    private ArrayList<String> userLikedId;
    private boolean isSub = false;
    private String id;

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

    public Review(JSONObject jsonObject){
        try{
            //this.user = new User(jsonObject.getJSONObject("user"));
            this.id = jsonObject.getString("_id");
            this.userId = jsonObject.getString("user");
            this.comment = jsonObject.getString("message");
            int comment = jsonObject.getInt("comment_type");

            if(comment == 1){
                commentType = CommentType.COMMENT;
                this.uri = this.uri = Uri.parse("No uri broh");
            } else if(comment == 2) {
                commentType = CommentType.COMMENT_AND_IMAGE;
                this.uri = Uri.parse(jsonObject.getString("uri"));
            } else {
                commentType = CommentType.COMMENT_AND_GIF;
                this.uri = Uri.parse(jsonObject.getString("uri"));
            }

            JSONArray userLiked = jsonObject.getJSONArray("user_liked");
            this.userLikedId = new ArrayList<>();
            for (int i = 0; i < userLiked.length(); i++){
                this.userLikedId.add(userLiked.getString(i));
            }

            this.userLiked = new ArrayList<>();

            JSONArray subReviews = jsonObject.getJSONArray("subreviews");
            this.subReviewsId = new ArrayList<>();
            for (int i = 0; i < subReviews.length(); i++){
                this.subReviewsId.add(subReviews.getString(i));
            }
        } catch (Error e){
            System.out.println("Error creating user from json object");
        } catch (JSONException ex) {
            System.out.println("Error creating user from json object. Catching json");
        }
    }

    public void setIsSub(boolean isSub){
        this.isSub = isSub;
    }

    public JSONObject toJsonObject(){
        int type = commentTypeToInt();

        JSONObject jsonObject = new JSONObject();
        try {
            //this.id = this.userId +
            if(id != null)
                jsonObject.put("_id", this.id);
            /*} else {
                Random r = new Random();
                String commentId = user.getUid()  + r.nextInt((9999 - 0) + 1) + 0;
                jsonObject.put("_id", commentId);
            }*/
            jsonObject.put("message", comment);
            jsonObject.put("user", user.getUid());
            jsonObject.put("uri", uri);
            jsonObject.put("comment_type", type);
            jsonObject.put("is_sub", isSub);
            JSONArray liked = new JSONArray();
            for(User lik : userLiked){
                liked.put(lik.getUid());
            }
            jsonObject.put("user_liked", liked);
            JSONArray subReviewIdJson = new JSONArray();
            if(subReviewsId == null)
                subReviewsId = new ArrayList<>();
            for(String subReviewId: subReviewsId){
                subReviewIdJson.put(subReviewId);
            }
            jsonObject.put("subreviews", subReviewIdJson);

            /*  "_id": "5df8e808db179a094b66c188",
            "message": "Great book!",
            "subreviews": [],
            "request": {
                "type": "GET",
                "url": "http://localhost:3000/comments/5df8e808db179a094b66c188"
            }*/
        } catch (Exception e) {
            System.out.println("seti");
        }
        return jsonObject;
    }

    public void setId(String id){
        this.id = id;
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
        if(this.subReviews == null)
            this.subReviews = new ArrayList<>();
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
