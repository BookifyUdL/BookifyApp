package com.example.readify.Models;

import org.json.JSONObject;

public class Author {

    private String name;
    private String id;

    public Author(JSONObject author){
        try{
            this.id = author.getString("_id");
            this.name = author.getString("name");
        } catch (Exception e) {
            this.name = "Error";
            this.id = "Error";
        }
    }

    public Author(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public Author(){

    }

    public JSONObject toJson(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", getId());
            jsonObject.put("name", getName());

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
