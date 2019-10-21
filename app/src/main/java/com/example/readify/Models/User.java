package com.example.readify.Models;

import java.util.ArrayList;

public class User {
    ArrayList<Book> library;

    public User() {
        library = new ArrayList<>();
    }

    public User(ArrayList<Book> arrayList) {
        if (arrayList != null)
            library = arrayList;
    }

    public ArrayList<Book> getLibrary() {
        return library;
    }

    public void setLibrary(ArrayList<Book> library) {
        this.library = library;
    }

    public void addBookToLibrary(Book book){
        library.add(book);
    }

    public void removeBookToLibrary(Book book){
        library.remove(book);
    }

    public boolean containsBook(Book book) {
        for (int i = 0; i < library.size(); i++)
            if (library.get(i).equals(book))
                return true;
        return false;
    }
}
