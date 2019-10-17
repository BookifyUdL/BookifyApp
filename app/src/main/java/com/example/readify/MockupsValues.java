package com.example.readify;

import java.util.ArrayList;

public class MockupsValues {
    private static ArrayList<String> GENRE;

    public static ArrayList<String> getGenres(){
        if (GENRE == null || GENRE.isEmpty()){
            GENRE = new ArrayList<>();
            GENRE.add("Biography");
            GENRE.add("Computing & internet");
            GENRE.add("Crime & thrillers");
            GENRE.add("Education");
            GENRE.add("Fiction & Literature");
            GENRE.add("Health & well-being");
            GENRE.add("Romance");
            GENRE.add("Sci-fi & Fantasy");
            GENRE.add("Travel & Adventure");
        }
        return GENRE;
    }
}
