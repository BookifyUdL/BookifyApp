package com.example.readify;

import android.content.Context;

import com.example.readify.Models.Book;
import com.example.readify.Models.Genre;
import com.example.readify.Models.User;

import java.util.ArrayList;
import java.util.List;

public class MockupsValues {

    private static ArrayList<Genre> GENRE;
    private static ArrayList<Book> ALL_BOOKS_FOR_TUTORIAL;
    private static ArrayList<Book> LAST_ADDED_BOOKS;
    private static ArrayList<Book> SAME_AUTHOR_BOOKS;
    private static ArrayList<Book> SAME_GENDER_BOOKS;
    private static ArrayList<Book> PENDING_LIST_BOOKS;
    private static ArrayList<Book> READING_LIST_BOOKS;
    private static ArrayList<Book> PENDING_BOOKS_WITH_DISCOVER_BUTTON;
    public  static User user = new User();
    private static Context context;

    public static void setContext(Context con){
        context = con;
    }

    public static User getUser(){
        return user;
    }

    //public void setUser(User user) { this.user = user; }

    public static ArrayList<Genre> getGenres() {
        if (GENRE == null || GENRE.isEmpty()) {
            GENRE = new ArrayList<>();
            GENRE.add(new Genre( context.getResources().getString(R.string.biography_gender), "genre1"));
            GENRE.add(new Genre( context.getResources().getString(R.string.internet_gender), "genre2"));
            GENRE.add(new Genre( context.getResources().getString(R.string.crime_gender), "genre3"));
            GENRE.add(new Genre( context.getResources().getString(R.string.education_gender), "genre4"));
            GENRE.add(new Genre( context.getResources().getString(R.string.literature_gender), "genre5"));
            GENRE.add(new Genre( context.getResources().getString(R.string.health_gender), "genre6"));
            GENRE.add(new Genre( context.getResources().getString(R.string.romance_gender), "genre7"));
            GENRE.add(new Genre( context.getResources().getString(R.string.fantasy_gender), "genre8"));
            GENRE.add(new Genre( context.getResources().getString(R.string.adventure_gender), "genre9"));
        }
        return GENRE;
    }

    public static Genre getGenreFromGenresList(int i){
        return getGenres().get(i);
    }
  
    public static void addPendingBook(Book book){
        if(PENDING_LIST_BOOKS == null)
            PENDING_LIST_BOOKS = new ArrayList<>();
        PENDING_LIST_BOOKS.add(book);
    }

    public static void removePendingListBook(Book book){
        if(PENDING_LIST_BOOKS == null)
            PENDING_LIST_BOOKS = new ArrayList<>();
        PENDING_LIST_BOOKS.remove(book);
    }

    public static void removeReadingListBook(Book book){
        if(READING_LIST_BOOKS == null)
            READING_LIST_BOOKS = new ArrayList<>();
        READING_LIST_BOOKS.remove(book);
    }

    public static  void addReadingBook(Book book){
        if(READING_LIST_BOOKS== null)
            READING_LIST_BOOKS = new ArrayList<>();
        READING_LIST_BOOKS.add(book);
    }

    public static void setPendingListBooks(ArrayList<Book> books){
        PENDING_LIST_BOOKS = books;
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

    public static ArrayList<Book> getAllBooksForTutorial(){
        if(ALL_BOOKS_FOR_TUTORIAL == null || ALL_BOOKS_FOR_TUTORIAL.isEmpty()){
            ALL_BOOKS_FOR_TUTORIAL = new ArrayList<>();
            ALL_BOOKS_FOR_TUTORIAL.addAll(getLastAddedBooks());
            ALL_BOOKS_FOR_TUTORIAL.addAll(getSameAuthorBooks());
            ALL_BOOKS_FOR_TUTORIAL.addAll(getSameGenderBooks());
        }
        return ALL_BOOKS_FOR_TUTORIAL;
    }


    public static ArrayList<Book> getLastAddedBooks(){
        if(LAST_ADDED_BOOKS == null || LAST_ADDED_BOOKS.isEmpty()){
            LAST_ADDED_BOOKS = new ArrayList<>();
            LAST_ADDED_BOOKS.add(new Book("Algún día hoy", "Ángela Becerra", "lib1", 2015, 253,getGenreFromGenresList(6)));
            LAST_ADDED_BOOKS.add(new Book("La cocinera de Gastamar", "Fernadndo J.Múñez", "lib2", 2001, 200,getGenreFromGenresList(3)));
            LAST_ADDED_BOOKS.add(new Book("El rey recibe", "Eduardo Mendoza", "lib3", 2017, 353,getGenreFromGenresList(4)));
            LAST_ADDED_BOOKS.add(new Book("100 recetas de oro", "Carlos Arguiñano", "lib4", 2019, 153,getGenreFromGenresList(5)));
            LAST_ADDED_BOOKS.add(new Book("Patria", "Fernando Aramburu", "lib5", 2018, 99,getGenreFromGenresList(7)));
        }
        return LAST_ADDED_BOOKS;
    }

    public static ArrayList<Book> getLastAddedBooksDiscover(){
        if(PENDING_BOOKS_WITH_DISCOVER_BUTTON == null || PENDING_BOOKS_WITH_DISCOVER_BUTTON.isEmpty()){
            PENDING_BOOKS_WITH_DISCOVER_BUTTON = new ArrayList<>();
            PENDING_BOOKS_WITH_DISCOVER_BUTTON.addAll(getLastAddedBooks());
            PENDING_BOOKS_WITH_DISCOVER_BUTTON.add(new Book());
      }
        return LAST_ADDED_BOOKS;
    }

    public static ArrayList<Book> getSameAuthorBooks(){
        if(SAME_AUTHOR_BOOKS == null || SAME_AUTHOR_BOOKS.isEmpty()){
            SAME_AUTHOR_BOOKS = new ArrayList<>();
            SAME_AUTHOR_BOOKS.add(new Book("El penúltimo sueño", "Ángela Becerra", "auth1", 2011, 289,getGenreFromGenresList(6)));
            SAME_AUTHOR_BOOKS.add(new Book("Lo que le falta al tiempo", "Ángela Becerra", "auth2", 2015, 129,getGenreFromGenresList(4)));
            SAME_AUTHOR_BOOKS.add(new Book("Memorias de un singerguenza de siete suelas", "Ángela Becerra", "auth3", 2005, 189,getGenreFromGenresList(6)));
            SAME_AUTHOR_BOOKS.add(new Book("Ella, que todo lo tuvo", "Ángela Becerra", "auth4", 2019, 444,getGenreFromGenresList(4)));
            SAME_AUTHOR_BOOKS.add(new Book("De los amores negados", "Ángela Becerra", "auth5", 2007, 299,getGenreFromGenresList(4)));
        }
        return SAME_AUTHOR_BOOKS;
    }

    public static ArrayList<Book> getSameGenderBooks(){
        if(SAME_GENDER_BOOKS == null || SAME_GENDER_BOOKS.isEmpty()){
            SAME_GENDER_BOOKS = new ArrayList<>();
            SAME_GENDER_BOOKS.add(new Book("Largo pétalo de mar", "Isabel Allende", "oth1", 2005, 189,getGenreFromGenresList(6)));
            SAME_GENDER_BOOKS.add(new Book("Reina Roja", "Juan Gómez-Jurado", "oth2", 2015, 461,getGenreFromGenresList(6)));
            SAME_GENDER_BOOKS.add(new Book("Silencio", "Pablo Poveda", "oth3", 2016, 501,getGenreFromGenresList(6)));
            SAME_GENDER_BOOKS.add(new Book("Eres parte de mí", "Saint Rose Sophie", "oth4", 2018, 235,getGenreFromGenresList(6)));
            SAME_GENDER_BOOKS.add(new Book("7 Libros para Eva", "Roberto Martínez Guzmán", "oth5", 2011, 223,getGenreFromGenresList(6)));

        }
        return SAME_GENDER_BOOKS;
    }
}
