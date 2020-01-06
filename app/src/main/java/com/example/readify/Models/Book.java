package com.example.readify.Models;

import android.content.Context;

import com.example.readify.R;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Book implements Serializable {
    private String title;
    private String author;
    private String picture;
    private String summary;
    private boolean read;
    private int year;
    private int extension;
    private Genre genre;
    public Author auth;
    private ArrayList<Review> comments;
    //private ArrayList<Review>
    private String id;
    private int sumRatings;
    private int numRatings;
    private boolean isNew;
    private ArrayList<Emoji> emojis;
    private Calendar calendar;


    public Book(String id, String title, Author author, String picture, boolean isNew){
        this.id = id;
        this.title = title;
        this.auth = author;
        this.picture = picture;
        this.author = author.getName();
        this.isNew = isNew;
    }

    public Book(JSONObject jsonobject, Context context){
        try{
            this.title = jsonobject.getString("title");
            this.summary = jsonobject.getString("summary");
            this.id = jsonobject.getString("_id");
            this.picture = jsonobject.getString("cover_image");
            this.sumRatings = jsonobject.getInt("rating");
            this.numRatings = jsonobject.getInt("num_rating");
            this.isNew = jsonobject.getBoolean("is_new");
            //String aux = jsonobject.get("genre").toString();
            JSONArray jsonArrayGenre = jsonobject.getJSONArray("genre");
            try {
                JSONObject jsonObjectGenre = jsonArrayGenre.getJSONObject(0);
                this.genre = new Genre(jsonArrayGenre.getJSONObject(0));   //new JSONArray();
            } catch (Exception e) {
                this.genre = new Genre("5de7fb595a66a02fe3c39eae", "Crime", "genre3");
            }
            this.extension = jsonobject.getInt("num_page");
            this.auth = new Author(jsonobject.getJSONObject("author"));
            this.author = this.auth.getName();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.ENGLISH);
            try{
                Date date = format.parse(jsonobject.getString("publication_date"));
                this.calendar = new GregorianCalendar();
                calendar.setTime(date);
                this.year = calendar.get(Calendar.YEAR);
            } catch (Exception e) {
                this.year = 2019;
            }
            JSONObject feelings = jsonobject.getJSONObject("feelings");
            setEmojis(feelings, context);
            comments = new ArrayList<>();
            JSONArray commentsJson = jsonobject.getJSONArray("comments");
            for(int i = 0; i < commentsJson.length(); i++){
                comments.add(new Review(commentsJson.getJSONObject(i)));
            }

            /*public Emoji(String name, String emoji, int value, int num){
                this.name = name;
                this.emoji = emoji;
                this.value = value;
                this.num = num;
            }*/



            /*public static ArrayList<Emoji> getEmojis(){
                if(EMOJIS == null || EMOJIS.isEmpty()){
                    EMOJIS = new ArrayList<>();
                    EMOJIS.add(new Emoji(context.getResources().getString(R.string.angry_emoji), "angry"));
                    EMOJIS.add(new Emoji(context.getResources().getString(R.string.scared_emoji), "scare"));
                    EMOJIS.add(new Emoji(context.getResources().getString(R.string.sad_emoji), "unhappy"));
                    EMOJIS.add(new Emoji(context.getResources().getString(R.string.confused_emoji), "confused"));
                    EMOJIS.add(new Emoji(context.getResources().getString(R.string.bored_emoji), "bored"));
                    EMOJIS.add(new Emoji(context.getResources().getString(R.string.shocked_emoji), "surprised"));
                    EMOJIS.add(new Emoji(context.getResources().getString(R.string.happy_emoji), "happy"));
                    EMOJIS.add(new Emoji(context.getResources().getString(R.string.excited_emoji), "excited"));
                }
                return EMOJIS;*/
            //Date date  = Date.from(jsonobject.getString("publication_date"));
            //this.year = 1;
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
            System.out.println("Error parsing book constructor");
            //this.name = "Error";
            //this.picture = "Error";
        }
    }

    public Book(JSONObject jsonobject, boolean type){
        try{
            this.title = jsonobject.getString("title");
            this.summary = jsonobject.getString("summary");
            this.id = jsonobject.getString("_id");
            this.picture = jsonobject.getString("cover_image");
            this.sumRatings = jsonobject.getInt("rating");
            this.numRatings = jsonobject.getInt("num_rating");
            this.isNew = jsonobject.getBoolean("is_new");
            //String aux = jsonobject.get("genre").toString();
            this.extension = jsonobject.getInt("num_page");
            //this.auth = new Author(jsonobject.getJSONObject("author"));
            this.author = jsonobject.getString("author");
        } catch (JSONException e) {
            System.out.println("Error parsing book constructor");
            //this.name = "Error";
            //this.picture = "Error";
        }
    }

    public void addRate(int rate){
        sumRatings = sumRatings + rate;
        numRatings = numRatings + 1;
        //return toJsonObject();
    }


    public void addComment(Review comment){
        if(comments == null)
            comments = new ArrayList<>();
        comments.add(comment);
    }

    public ArrayList<Review> getComments(){
        if(comments == null)
            comments = new ArrayList<>();
        return this.comments;
    }

    public JSONObject toJsonObject(){
        JSONObject jsonObject = new JSONObject();
        try {
            //Missing COMMENTS
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(this.getGenre().toJSON());
            jsonObject.put("genre", jsonArray);
            jsonObject.put("comments", new JSONArray());
            jsonObject.put("rating", sumRatings);
            jsonObject.put("num_rating", numRatings);
            jsonObject.put("is_new", isNew);
            jsonObject.put("_id", id);
            jsonObject.put("num_page", extension);
            jsonObject.put("author", auth.toJson());
            jsonObject.put("title", title);
            jsonObject.put("summary", summary);
            jsonObject.put("cover_image", picture);
            String date = this.year + "-01-01" + "T00:00:00.000Z";
            jsonObject.put("publication_date", date);
            //"publication_date": "2019-01-01T00:00:00.000Z"

            JSONArray feelingsArray = new JSONArray();
            JSONObject object = new JSONObject();
            object.put("angry", getEmojis().get(0).getValue());
            object.put("scared", getEmojis().get(1).getValue());
            object.put("sad", getEmojis().get(2).getValue());
            object.put("confused", getEmojis().get(3).getValue());
            object.put("bored", getEmojis().get(4).getValue());
            object.put("shocked", getEmojis().get(5).getValue());
            object.put("happy", getEmojis().get(6).getValue());
            object.put("excited", getEmojis().get(7).getValue());

            jsonObject.put("feelings", object);

            JSONArray commmentsArray = new JSONArray();
            for (Review comment : comments){
                commmentsArray.put(comment.toJsonObject());
            }
            jsonObject.put("comments", commmentsArray);


            return jsonObject;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return new JSONObject();
        }
    }

    public void setEmojis(JSONObject feelings, Context context){
        try {
            this.emojis = new ArrayList<>();
            int angry = feelings.getInt("angry");
            int scared = feelings.getInt("scared");
            int sad = feelings.getInt("sad");
            int confused = feelings.getInt("confused");
            int bored = feelings.getInt("bored");
            int shocked = feelings.getInt("shocked");
            int happy = feelings.getInt("happy");
            int excited = feelings.getInt("excited");

            emojis = new ArrayList<>();
            emojis.add(new Emoji(context.getResources().getString(R.string.angry_emoji), "angry", angry, numRatings));
            emojis.add(new Emoji(context.getResources().getString(R.string.scared_emoji), "scare", scared, numRatings));
            emojis.add(new Emoji(context.getResources().getString(R.string.sad_emoji), "unhappy", sad, numRatings));
            emojis.add(new Emoji(context.getResources().getString(R.string.confused_emoji), "confused", confused, numRatings));
            emojis.add(new Emoji(context.getResources().getString(R.string.bored_emoji), "bored", bored, numRatings));
            emojis.add(new Emoji(context.getResources().getString(R.string.shocked_emoji), "surprised", shocked, numRatings));
            emojis.add(new Emoji(context.getResources().getString(R.string.happy_emoji), "happy", happy, numRatings));
            emojis.add(new Emoji(context.getResources().getString(R.string.excited_emoji), "excited", excited, numRatings));

        } catch (Exception e){
            this.emojis = new ArrayList<>();
        }

    }

    public ArrayList<Emoji> getEmojis(){
        return this.emojis;
    }

    public String getSummary() {
        return summary;
    }

    public int getSumRatings() {
        return sumRatings;
    }

    public void setSumRatings(int sumRatings) {
        this.sumRatings = sumRatings;
    }

    public int getNumRatings() {
        return numRatings;
    }

    public void setNumRatings(int numRatings) {
        this.numRatings = numRatings;
    }

    public static ArrayList<Book> bookListFromJson(JSONArray bookListJson){
        try {
            ArrayList<Book> booksList = new ArrayList<>();
            for (int i=0; i < bookListJson.length(); i++){
                booksList.add(new Book(bookListJson.getJSONObject(i), false));
            }
            return  booksList;
        } catch (Exception e) {

        }
        return new ArrayList<>();
    }

    public static JSONArray bookListToJSON(ArrayList<Book> books){
        JSONArray booksArray = new JSONArray();
        if(books != null){
            for (Book book : books){
                JSONObject object = new JSONObject();
                try{
                    object.put("_id", book.getId());
                    booksArray.put(object);
                } catch (Exception e) {
                    //continue;
                }
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

    public Book(String id, String title, String author, String picture) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.picture = picture;
        this.read = false;
    }

    public Book() {
    }

    @Override
    public boolean equals(Object o){
        // If the object is compared with itself then return true
        if (o == this) {
            return true;
        }

        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
        if (!(o instanceof Book)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members
        Book c = (Book) o;

        // Compare the data members and return accordingly
        return c.getId().equals(this.getId());
    }


    public boolean isNew(){
        return this.isNew;
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
