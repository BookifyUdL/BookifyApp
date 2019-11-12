package com.example.readify.Models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;

import androidx.annotation.NonNull;

import com.example.readify.MockupsValues;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class User {
    private String uid;
    private String name;
    private String email;
    private String picture;
    private Boolean premium;
    private ArrayList<Achievement> achievements;
    private ArrayList<Book> library;
    private ArrayList<Genre> genres;

    private ArrayList<Book> interested;
    private ArrayList<Book> read;

    public User() {
        this.uid = "0000";
        this.name = "User Unknown";
        this.email = "user@unknown.com";
        this.picture = "userfinale";
        this.premium = false;
        this.library = new ArrayList<>();
        this.read = new ArrayList<>();
        this.interested = new ArrayList<>();
        this.genres = new ArrayList<>();
        this.achievements = new ArrayList<>();
        //this.achievements = MockupsValues.getAchievementsPersonalized();
    }

    public User(String name, String picture) {
        this.name = name;
        this.picture = picture;
    }

    public User(String name, Boolean premium, ArrayList<Genre> genres, ArrayList<Book> library) {
        this.name = name;
        //this.premium = premium;
        if (library != null)
            this.library = library;
        if (genres != null)
            this.genres = genres;
        this.achievements = MockupsValues.getAchievementsPersonalized();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean isPremium() {
        return premium;
    }

    public void setPremium(Boolean premium) {
        this.premium = premium;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addBookToInterestedBooks(Book book) {
        interested.add(book);
    }

    public ArrayList<Book> getReadBooks() {
        return read;
    }

    public void addBookToReadBooks(Book book) {
        read.add(book);
    }

    public ArrayList<Book> getInterestedBooks() {
        return interested;
    }

    public ArrayList<Genre> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<Genre> genres) {
        this.genres = genres;
    }

    public ArrayList<Book> getRead() {
        return read;
    }

    public void setRead(ArrayList<Book> read) {
        this.read = read;
    }

    public void addGenreToGenres(Genre genre) {
        genres.add(genre);
    }

    public void removeGenreToGenres(Genre genre) {
        library.remove(genre);
    }

    public boolean containsGenre(Genre genre) {
        for (int i = 0; i < genres.size(); i++)
            if (genres.get(i).equals(genre))
                return true;
        return false;
    }

    public ArrayList<Book> getLibrary() {
        return library;
    }

    public void setLibrary(ArrayList<Book> library) {
        this.library = library;
    }

    public void addBookToLibrary(Book book) {
        library.add(book);
    }

    public void removeBookToLibrary(Book book) {
        library.remove(book);
    }

    public boolean containsBook(Book book) {
        for (int i = 0; i < library.size(); i++)
            if (library.get(i).equals(book))
                return true;
        return false;
    }

    public ArrayList<Book> getReadedBooks() {
        ArrayList<Book> results = new ArrayList<>();
        Book book;

        ListIterator<Book> iterator = this.library.listIterator();
        while (iterator.hasNext()) {
            book = iterator.next();
            if (book.isRead())
                results.add(book);
        }

        return results;
    }

    public int getNumReadedBooks() {
        int numCompleted = 0;

        for (Book book : this.library) {
            if (book.isRead())
                numCompleted++;
        }

        return numCompleted;
    }

    public ArrayList<Achievement> getAchievements() {
        return achievements;
    }

    public void setAchievements(ArrayList<Achievement> achievements) {
        this.achievements = achievements;
    }

    public int getNumCompletedAchievements() {
        int numCompleted = 0;
        for (Achievement achievement : this.achievements) {
            if (achievement.isCompleted())
                numCompleted++;
        }
        return numCompleted;
    }

    public ArrayList<Achievement> getCompletedAchievements() {
        ArrayList<Achievement> results = new ArrayList<>();
        Achievement achievement;

        for (Achievement value : this.achievements) {
            achievement = value;
            if (achievement.isCompleted())
                results.add(achievement);
        }

        return results;
    }

    public void setLibraryBookAsRead(Book book) {
        //book.setRead(true);
        if (this.getLibrary().contains(book)) {
            this.getLibrary().remove(book);
        }
        book.setRead(true);
        this.getLibrary().add(0, book);
    }

    public ArrayList<Book> getInterested() {
        return interested;
    }

    public void setInterested(ArrayList<Book> interested) {
        this.interested = interested;
    }

    public void readFromSharedPreferences(SharedPreferences pref) {
        this.uid = pref.getString("com.example.readify.uid", "0");
        this.name = pref.getString("com.example.readify.name", "Unknown");
        this.email = pref.getString("com.example.readify.email", "mail@unknown.com");
        this.picture = pref.getString("com.example.readify.photo", "userfinale");
        this.premium = pref.getBoolean("com.example.readify.premium", false);

        //Library
        String libraryPref = pref.getString("com.example.readify.library", "");
        Type type = new TypeToken<List<Book>>() {}.getType();
        this.library = new Gson().fromJson(libraryPref, type);

        //Genres
        String genresPref = pref.getString("com.example.readify.genres", "");
        Type type1 = new TypeToken<List<Genre>>() {}.getType();
        this.genres = new Gson().fromJson(genresPref, type1);

        //Interested
        String interestedPref = pref.getString("com.example.readify.interested", "");
        Type type2 = new TypeToken<List<Book>>() {}.getType();
        this.interested = new Gson().fromJson(interestedPref, type2);

        //Achievements
        String achievementsPref = pref.getString("com.example.readify.achievements", "");
        Type type3 = new TypeToken<List<Achievement>>() {}.getType();
        this.achievements = new Gson().fromJson(achievementsPref, type3);
    }

    /* Method to update information on database */
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", this.uid);
        result.put("name", this.name);
        result.put("email", this.email);
        result.put("picture", this.picture);
        result.put("premium", this.premium);
        result.put("library", this.library);
        result.put("read", this.read);
        result.put("interested", this.interested);
        result.put("genres", this.genres);
        result.put("achievements", this.achievements);

        return result;
    }

    public void saveToFirebase(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(this.uid, this.toMap());
        database.getReference("users").updateChildren(childUpdates);
    }

}
