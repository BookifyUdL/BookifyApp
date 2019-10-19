package com.example.readify;

import com.example.readify.Models.Book;

import java.util.ArrayList;
import java.util.List;

public class MockupsValues {

    public static ArrayList<Book> LAST_ADDED_BOOKS;
    public static ArrayList<Book> SAME_AUTHOR_BOOKS;
    public static ArrayList<Book> SAME_GENDER_BOOKS;


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