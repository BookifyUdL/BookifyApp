package com.example.readify.Models;

import org.json.JSONObject;

public class Shop {

    public static String CORTE_INGLES_ID = "5de7fb5a5a66a02fe3c39eb6";
    public static String FNAC_ID = "5de7fb5b5a66a02fe3c39eb7";
    public static String CASA_DEL_LIBRO_ID = "5de7fb5a5a66a02fe3c39eb5";

    private String id;
    private String url;
    private String name;

    public Shop(String url, String name) {
        this.url = url;
        this.name = name;
    }

    public Shop(String id, String url, String name) {
        this.id = id;
        this.url = url;
        this.name = name;
    }

    public Shop(JSONObject shopJSON){
        try{
            this.id = shopJSON.getString("_id");
            this.url = shopJSON.getString("url");
            this.name = shopJSON.getString("name");
        } catch (Exception e){

        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
