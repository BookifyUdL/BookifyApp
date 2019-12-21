package com.example.readify.Models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;

import androidx.annotation.NonNull;

import com.example.readify.Discover.BooksSectionFragment;
import com.example.readify.MockupsValues;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private String firebaseId;
    private ArrayList<Book> interested;
    private ArrayList<Book> reading;

    public User() {
        //this.uid = "0000";
        this.name = "User Unknown";
        this.email = "user@unknown.com";
        this.picture = "userfinale";
        this.premium = false;
        this.library = new ArrayList<>();
        this.reading = new ArrayList<>();
        this.interested = new ArrayList<>();
        this.genres = new ArrayList<>();
        this.achievements = MockupsValues.getAchievementsPersonalized();
    }

    public User(String name, String picture) {
        this.name = name;
        this.email = "user@unknown.com";
        this.picture = picture;
        this.premium = false;
        this.library = new ArrayList<>();
        this.reading = new ArrayList<>();
        this.interested = new ArrayList<>();
        this.genres = new ArrayList<>();
        this.achievements = MockupsValues.getAchievementsPersonalized();
    }

    public User(String name, String email, String picture) {
        this.name = name;
        this.email = email;
        this.picture = "https://lh3.googleusercontent.com/a-/AAuE7mASENpnxywLWZs5rlleVOyYuAUGuw7RaajOI1Nt=s96-c";
        this.premium = false;
        this.library = new ArrayList<>();
        this.reading = new ArrayList<>();
        this.interested = new ArrayList<>();
        this.genres = new ArrayList<>();
        this.achievements = MockupsValues.getAchievementsPersonalized();
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

    public User(String uid, String firebaseId, String name, String picture, Boolean premium,
                String email, ArrayList<Genre> genres, ArrayList<Book> library, ArrayList<Book> reading,
                ArrayList<Book> interested, ArrayList<Achievement> achievements) {
        this.uid = uid;
        this.firebaseId = firebaseId;
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.premium = premium;
        this.library = library;
        this.reading = reading;
        this.interested = interested;
        this.genres = genres;
        this.achievements = achievements;
    }

    public User(JSONObject userJson) {
        try {
            JSONObject user = userJson.getJSONObject("genre");
            this.premium = user.getBoolean("premium");
            // MIssing Achivements
            this.achievements = new ArrayList<>();
            this.library = Book.bookListFromJson(userJson.getJSONArray("library"));
            this.reading = Book.bookListFromJson(userJson.getJSONArray("reading_book"));
            this.interested = Book.bookListFromJson(userJson.getJSONArray("interested_book"));
            this.uid = user.getString("_id");
            this.firebaseId = user.getString("firebaseId");
            this.picture = user.getString("userPicture");
            this.name = user.getString("name");
            this.email = user.getString("email");

            JSONArray genresId = user.getJSONArray("genres");
            this.genres = Genre.genresFromJSONArray(genresId);

        } catch (Error e) {
            System.out.println("Error creating user from json object");
        } catch (JSONException ex) {
            System.out.println("Error creating user from json object. Catching json");
        }
    }

    public void getInfoFromJSON(JSONObject userJson) {
        try {
            this.premium = userJson.getBoolean("premium");

            this.genres = Genre.genresFromJSONArray(userJson.getJSONArray("genres"));
            this.library = Book.bookListFromJson(userJson.getJSONArray("library"));
            this.interested = Book.bookListFromJson(userJson.getJSONArray("interested_book"));
            this.reading = Book.bookListFromJson(userJson.getJSONArray("reading_book"));
            this.achievements = MockupsValues.getAchievements();
        } catch (Exception ex) {
            System.out.println("Error adding information user from json object");
        }
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    public static JSONArray toJSONPatch(User user) {
        JSONArray jsonArray = new JSONArray();
        try {

            JSONObject nameJSON = new JSONObject();
            nameJSON.put("propName", "name");
            nameJSON.put("value", user.getName());
            jsonArray.put(nameJSON);

            //MISSING ACHIEVEMENTS
            JSONObject achievementsJSON = new JSONObject();
            achievementsJSON.put("propName", "achievements");
            achievementsJSON.put("value", new JSONArray());
            jsonArray.put(achievementsJSON);

            JSONObject firebaseIdJSON = new JSONObject();
            firebaseIdJSON.put("propName", "firebaseId");
            firebaseIdJSON.put("value", user.getFirebaseId());
            jsonArray.put(firebaseIdJSON);

            JSONObject userPictureJSON = new JSONObject();
            userPictureJSON.put("propName", "userPicture");
            userPictureJSON.put("value", user.getPicture());
            jsonArray.put(userPictureJSON);

            JSONObject premiumJSON = new JSONObject();
            premiumJSON.put("propName", "premium");
            premiumJSON.put("value", user.isPremium());
            jsonArray.put(premiumJSON);

            JSONObject libraryJSON = new JSONObject();
            libraryJSON.put("propName", "library");
            libraryJSON.put("value", Book.bookListToJSON(user.getLibrary()));
            jsonArray.put(libraryJSON);

            JSONObject readBooksJSON = new JSONObject();
            readBooksJSON.put("propName", "read_book");
            readBooksJSON.put("value", Book.bookListToJSON(user.getReadedBooks()));
            jsonArray.put(readBooksJSON);


            JSONObject interestedBooksJSON = new JSONObject();
            interestedBooksJSON.put("propName", "interested_book");
            interestedBooksJSON.put("value", Book.bookListToJSON(user.getInterested()));
            jsonArray.put(interestedBooksJSON);

            JSONObject readingBooksJSON = new JSONObject();
            readingBooksJSON.put("propName", "reading_book");
            readingBooksJSON.put("value", Book.bookListToJSON(user.getReadingBooks()));
            jsonArray.put(readBooksJSON);

            JSONObject emailJSON = new JSONObject();
            emailJSON.put("propName", "email");
            emailJSON.put("value", user.getEmail());
            jsonArray.put(emailJSON);

            JSONObject genresJSON = new JSONObject();
            genresJSON.put("propName", "genres");
            genresJSON.put("value", Genre.genresListToJSON(user.getGenres()));
            jsonArray.put(genresJSON);

            return jsonArray;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return new JSONArray();
        }
    }

    public static JSONObject toJSON(User user) {

        JSONObject jsonObject = new JSONObject();
        try {
            if (user.uid != null)
                jsonObject.put("_id", user.getUid());
            jsonObject.put("firebaseId", user.getFirebaseId());
            jsonObject.put("name", user.getName());
            jsonObject.put("userPicture", user.getPicture());
            jsonObject.put("email", user.getEmail());
            jsonObject.put("genres", Genre.genresListToJSON(user.getGenres()));
            jsonObject.put("premium", user.isPremium());

            jsonObject.put("library", Book.bookListToJSON(user.getLibrary()));
            jsonObject.put("read_book", Book.bookListToJSON(user.getReadedBooks()));
            jsonObject.put("interested_book", Book.bookListToJSON(user.getInterested()));
            jsonObject.put("reading_book", Book.bookListToJSON(user.getReadingBooks()));

            //MISSING ACHIEVEMENTS
            jsonObject.put("achievements", new JSONArray());

            return jsonObject;

        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONObject();
        }

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

    public void removeBookInterestedBooks(Book book) {
        interested.remove(book);
    }

    public void addBookToInterestedBooks(Book book) {
        interested.add(book);
    }

    public ArrayList<Book> getReadingBooks() {
        if (this.reading == null)
            this.reading = new ArrayList<>();
        return reading;
    }

    public void addBookToReadingBooks(Book book) {
        reading.add(book);
    }

    public ArrayList<Genre> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<Genre> genres) {
        this.genres = genres;
    }

    public ArrayList<Book> getReading() {
        if (this.reading == null)
            this.reading = new ArrayList<>();
        return reading;
    }

    public void setReading(ArrayList<Book> read) {
        this.reading = read;
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
        ArrayList<Book> lib = new ArrayList<>();
        lib.addAll(getReadingBooks());
        for (Book book : getInterested()) {
            if (!lib.contains(book))
                lib.add(book);
        }
        for (Book book : getReadedBooks()) {
            if (!lib.contains(book))
                lib.add(book);
        }
        for (Book book : getInterested()) {
            if (!lib.contains(book))
                lib.add(book);
        }
        return lib;
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
        if (this.interested == null)
            this.interested = new ArrayList<>();
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

        //Reading
        String readingPref = pref.getString("com.example.readify.reading", "");
        Type type4 = new TypeToken<List<Book>>() {}.getType();
        this.reading = new Gson().fromJson(readingPref, type4);
    }

    /* Method to update information on database */
    private Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", this.uid);
        result.put("idFirebase", this.firebaseId);
        result.put("name", this.name);
        result.put("email", this.email);
        result.put("picture", this.picture);
        result.put("premium", this.premium);
        result.put("library", this.library);
        result.put("interested", this.interested);
        result.put("reading", this.reading);
        result.put("genres", this.genres);
        result.put("achievements", this.achievements);

        return result;
    }

    public void saveToFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(this.uid, this.toMap());
        database.getReference("users").updateChildren(childUpdates);
    }

}
