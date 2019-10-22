package com.example.readify;

import com.example.readify.Models.Book;
import com.example.readify.Models.Genre;
import com.example.readify.Models.User;

import java.util.ArrayList;
import java.util.List;

public class MockupsValues {

    private static ArrayList<Genre> GENRE;
    private static ArrayList<Book> LAST_ADDED_BOOKS;
    private static ArrayList<Book> SAME_AUTHOR_BOOKS;
    private static ArrayList<Book> SAME_GENDER_BOOKS;
    private static ArrayList<Book> PENDING_LIST_BOOKS;
    private static ArrayList<Book> READING_LIST_BOOKS;
    public static User user = new User();

    public static User getUser(){
        return user;
    }

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
  
    public static void addPendingBook(Book book){
        if(PENDING_LIST_BOOKS == null)
            PENDING_LIST_BOOKS = new ArrayList<>();
        PENDING_LIST_BOOKS.add(book);
    }

    public static void removePendingListBook(Book book){
        PENDING_LIST_BOOKS.remove(book);
    }

    public static  void addReadingBook(Book book){
        if(READING_LIST_BOOKS== null)
            READING_LIST_BOOKS = new ArrayList<>();
        READING_LIST_BOOKS.add(book);
    }

    public static ArrayList<Book> getPendingListBooks(){
        if(PENDING_LIST_BOOKS == null){
            PENDING_LIST_BOOKS = new ArrayList<>();
        }
        return PENDING_LIST_BOOKS;
    }

    public static ArrayList<Book> getReandingListBooks(){
        if(READING_LIST_BOOKS == null){
            READING_LIST_BOOKS = new ArrayList<>();
        }
        return READING_LIST_BOOKS;
    }


    public static ArrayList<Book> getLastAddedBooks(){
        if(LAST_ADDED_BOOKS == null || LAST_ADDED_BOOKS.isEmpty()){
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

    public static ArrayList<Book> getSameAuthorBooks(){
        if(SAME_AUTHOR_BOOKS == null || SAME_AUTHOR_BOOKS.isEmpty()){
            SAME_AUTHOR_BOOKS = new ArrayList<>();
            SAME_AUTHOR_BOOKS.add(new Book("El penúltimo sueño", "Ángela Becerra", "auth1"));
            SAME_AUTHOR_BOOKS.add(new Book("Lo que le falta al tiempo", "Ángela Becerra", "auth2"));
            SAME_AUTHOR_BOOKS.add(new Book("Memorias de un singerguenza de siete suelas", "Ángela Becerra", "auth3"));
            SAME_AUTHOR_BOOKS.add(new Book("Ella, que todo lo tuvo", "Ángela Becerra", "auth4"));
            SAME_AUTHOR_BOOKS.add(new Book("De los amores negados", "Ángela Becerra", "auth5"));
        }
        return SAME_AUTHOR_BOOKS;
    }

    public static ArrayList<Book> getSameGenderBooks(){
        if(SAME_GENDER_BOOKS == null || SAME_GENDER_BOOKS.isEmpty()){
            SAME_GENDER_BOOKS = new ArrayList<>();
            SAME_GENDER_BOOKS.add(new Book("Largo pétalo de mar", "Isabel Allende", "oth1"));
            SAME_GENDER_BOOKS.add(new Book("Reina Roja", "Juan Gómez-Jurado", "oth2"));
            SAME_GENDER_BOOKS.add(new Book("Silencio", "Pablo Poveda", "oth3"));
            SAME_GENDER_BOOKS.add(new Book("Eres parte de mí", "Saint Rose Sophie", "oth4"));
            SAME_GENDER_BOOKS.add(new Book("7 Libros para Eva", "Roberto Martínez Guzmán", "oth5"));

        }
        return SAME_GENDER_BOOKS;
    }
}
