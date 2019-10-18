package com.example.readify;

import com.example.readify.Models.Genre;

import java.util.ArrayList;

public class MockupsValues {
    private static ArrayList<Genre> GENRE;

    public static ArrayList<Genre> getGenres(){
        if (GENRE == null || GENRE.isEmpty()){
            GENRE = new ArrayList<>();
            GENRE.add(new Genre("Biography", "genre1"));
            GENRE.add(new Genre("Computing & internet", "genre2"));
            GENRE.add(new Genre("Crime & thrillers","genre3"));
            GENRE.add(new Genre("Education", "genre4"));
            GENRE.add(new Genre("Fiction & Literature", "genre5"));
            GENRE.add(new Genre("Health & well-being","genre6"));
            GENRE.add(new Genre("Romance", "genre7"));
            GENRE.add(new Genre("Sci-fi & Fantasy", "genre8"));
            GENRE.add(new Genre("Travel & Adventure", "genre9"));
        }
        return GENRE;
    }
}
