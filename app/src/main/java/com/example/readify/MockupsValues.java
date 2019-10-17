package com.example.readify;

import com.example.readify.Models.Book;

import java.util.ArrayList;
import java.util.List;

public class MockupsValues {

    public static ArrayList<Book> LAST_ADDED_BOOKS;


    public static ArrayList<Book> getLastAddedBooks(){
        if(LAST_ADDED_BOOKS == null || LAST_ADDED_BOOKS.isEmpty()){
            LAST_ADDED_BOOKS = new ArrayList<>();
            LAST_ADDED_BOOKS.add(new Book("Algún día hoy", "Ángela Becerra", "lib1"));
            LAST_ADDED_BOOKS.add(new Book("La cocinera de Gastamar", "Fernadndo J.Múñez", "lib2"));
            LAST_ADDED_BOOKS.add(new Book("El rey recibe", "Eduardo Mendoza", "lib3"));
            LAST_ADDED_BOOKS.add(new Book("100 recetas de oro", "Carlos Arguiñano", "lib4"));
            LAST_ADDED_BOOKS.add(new Book("Patria", "Fernando Aramburu", "lib5"));

            /*Ronda dos*/
            LAST_ADDED_BOOKS.add(new Book("Algún día hoy", "Ángela Becerra", "lib1"));
            LAST_ADDED_BOOKS.add(new Book("La cocinera de Gastamar", "Fernadndo J.Múñez", "lib2"));
            LAST_ADDED_BOOKS.add(new Book("El rey recibe", "Eduardo Mendoza", "lib3"));
            LAST_ADDED_BOOKS.add(new Book("100 recetas de oro", "Carlos Arguiñano", "lib4"));
            LAST_ADDED_BOOKS.add(new Book("Patria", "Fernando Aramburu", "lib5"));
        }
        return LAST_ADDED_BOOKS;

        /*
        *  public Book(String title, String author, String picture) {
        this.title = title;
        this.author = author;
        this.picture = picture;
    }*/

    }
}