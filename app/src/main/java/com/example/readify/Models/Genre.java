package com.example.readify.Models;

import org.json.JSONException;
import org.json.JSONObject;

public class Genre {
    String name;
    String picture;

    public Genre() { }

    public Genre(String name, String picture) {
        this.name = name;
        this.picture = picture;
    }

    public Genre(JSONObject jsonobject){
        try{
            this.name = jsonobject.getString("name");
            this.picture = jsonobject.getString("picture");
        } catch (JSONException e) {
            this.name = "Error";
            this.picture = "Error";
        }
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
}
