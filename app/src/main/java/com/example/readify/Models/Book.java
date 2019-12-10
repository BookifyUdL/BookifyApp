package com.example.readify.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Book {
    private String title;
    private String author;
    private String picture;
    private String summary;
    private boolean read;
    private int year;
    private int extension;
    private Genre genre;
    private Author auth;
    private ArrayList<Review> comments;
    private String id;
    private int sumRatings;
    private int numRatings;
    private boolean isNew;


    public Book(String id, String title, Author author, String picture){
        this.id = id;
        this.title = title;
        this.auth = author;
        this.picture = picture;
        this.author = author.getName();
    }

    public Book(JSONObject jsonobject){
        try{
            this.title = jsonobject.getString("title");
            this.summary = jsonobject.getString("summary");
            this.id = jsonobject.getString("_id");
            this.picture = jsonobject.getString("cover_image");
            this.sumRatings = jsonobject.getInt("rating");
            this.numRatings = jsonobject.getInt("num_rating");
            this.isNew = jsonobject.getBoolean("is_new");
            //String aux = jsonobject.get("genre").toString();
            this.genre = new Genre(jsonobject.getJSONObject("genre"));
            //missing author
            //missing comments

           /* "publication_date": "2019-01-01T00:00:00.000Z",
                    "author": {
                "_id": "5ddd69ac99439a0f2d99edc9",
                        "name": "DEFREDS JOSE. A. GOMEZ IGLESIAS"
            },
            "genre": [
            {
                "_id": "5de7fb595a66a02fe3c39ead",
                    "picture": "genre2",
                    "name": "Computing / Interenet"
            }
            ],
            "cover_image": "https://imagessl6.casadellibro.com/a/l/t1/16/9788467056716.jpg",
                    "comments": [],
            "rating": 5,
                    "num_rating": 1,
                    "is_new": true,
                    "request": {
                "type": "GET",
                        "url": "http://localhost:3000/books/5de7fb655a66a02fe3c39ebc"
            }*/
        } catch (JSONException e) {
            //this.name = "Error";
            //this.picture = "Error";
        }
    }

    public static JSONArray bookListToJSON(ArrayList<Book> books){
        JSONArray booksArray = new JSONArray();
        for (Book book : books){
            JSONObject object = new JSONObject();
            try{
                //object.put("_id", book.getId());
                booksArray.put(object);
            } catch (Exception e) {
                //continue;
            }
        }
        return booksArray;
    }

    public Book(String title, String author, String picture, int year, int extension, Genre genre) {
        this.title = title;
        this.author = author;
        this.picture = picture;
        this.year = year;
        this.read = false;
        this.genre = genre;
        this.extension = extension;
    }

    public Book(String title, String author, String picture, int year) {
        this.title = title;
        this.author = author;
        this.picture = picture;
        this.year = year;
        this.read = false;
    }

    public Book(String title, String author, String picture) {
        this.title = title;
        this.author = author;
        this.picture = picture;
        this.read = false;
    }

    public Book() {
    }

    public int getExtension() { return this.extension; }

    public void setExtension(int extension){
        this.extension = extension;
    }

    public String getId(){
        return this.id;
    }

    public Genre getGenre() { return this.genre; }

    public void setGenre(Genre genre) { this.genre = genre; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    /*public Author getAuth(){
        return this.auth;
    }*/

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
