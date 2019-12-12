package com.example.readify.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Genre {
    String id;
    String name;
    String picture;

    public Genre() { }

    public Genre(String name, String picture) {
        this.name = name;
        this.picture = picture;
    }

    public Genre(JSONObject jsonobject){
        try{
            this.id = jsonobject.getString("_id");
            this.name = jsonobject.getString("name");
            this.picture = jsonobject.getString("picture");
        } catch (JSONException e) {
            this.name = "Error";
            this.picture = "Error";
        }
    }

    public static ArrayList<Genre> genresFromJSONArray(JSONArray jsonArray){
        ArrayList<Genre> genres = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject genre = jsonArray.getJSONObject(i);
                genres.add(new Genre(genre));
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return genres;
    }



    public static JSONArray  genresListToJSON(ArrayList<Genre> genres){
        JSONArray genresArray = new JSONArray();
        for (Genre genre : genres){
            JSONObject object = new JSONObject();
            try{
                object.put("_id", genre.getId());
                genresArray.put(object);
            } catch (Exception e) {
                //continue;
            }
        }
        return genresArray;
    }

    public JSONObject toJSON(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", getId());
            jsonObject.put("name", getName());
            jsonObject.put("picture", getPicture());

        } catch (Exception e){
            jsonObject = new JSONObject();
        }
        return jsonObject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getId(){
        return this.id;
    }
}
