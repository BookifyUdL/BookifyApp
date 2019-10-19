package com.example.readify;

import com.example.readify.Models.Book;
import com.example.readify.Models.Genre;

import java.util.ArrayList;

public class MockupsValues {
    private static ArrayList<Genre> GENRE;
    private static ArrayList<Book> LAST_ADDED_BOOKS;

    public static ArrayList<Genre> getGenres() {
        if (GENRE == null || GENRE.isEmpty()) {
            GENRE = new ArrayList<>();
            GENRE.add(new Genre("Biography", "genre1"));
            GENRE.add(new Genre("Computing & internet", "genre2"));
            GENRE.add(new Genre("Crime & thrillers", "genre3"));
            GENRE.add(new Genre("Education", "genre4"));
            GENRE.add(new Genre("Fiction & Literature", "genre5"));
            GENRE.add(new Genre("Health & well-being", "genre6"));
            GENRE.add(new Genre("Romance", "genre7"));
            GENRE.add(new Genre("Sci-fi & Fantasy", "genre8"));
            GENRE.add(new Genre("Travel & Adventure", "genre9"));
        }
        return GENRE;
    }

    public static ArrayList<Book> getLastAddedBooks() {
        if (LAST_ADDED_BOOKS == null || LAST_ADDED_BOOKS.isEmpty()) {
            LAST_ADDED_BOOKS = new ArrayList<>();
            LAST_ADDED_BOOKS.add(new Book("Algún día hoy", "Ángela Becerra", "lib1"));
            LAST_ADDED_BOOKS.add(new Book("La cocinera de Gastamar", "Fernadndo J.Múñez", "lib2"));
            LAST_ADDED_BOOKS.add(new Book("El rey recibe", "Eduardo Mendoza", "lib3"));
            LAST_ADDED_BOOKS.add(new Book("100 recetas de oro", "Carlos Arguiñano", "lib4"));
            LAST_ADDED_BOOKS.add(new Book("Patria", "Fernando Aramburu", "lib5"));

            /*Ronda dos*/
            /*LAST_ADDED_BOOKS.add(new Book("Algún día hoy", "Ángela Becerra", "lib1"));
            LAST_ADDED_BOOKS.add(new Book("La cocinera de Gastamar", "Fernadndo J.Múñez", "lib2"));
            LAST_ADDED_BOOKS.add(new Book("El rey recibe", "Eduardo Mendoza", "lib3"));
            LAST_ADDED_BOOKS.add(new Book("100 recetas de oro", "Carlos Arguiñano", "lib4"));
            LAST_ADDED_BOOKS.add(new Book("Patria", "Fernando Aramburu", "lib5"));*/
        }
        return LAST_ADDED_BOOKS;
    }
}
