package com.example.readify.Models;

import com.example.readify.MockupsValues;

import java.util.ArrayList;
import java.util.ListIterator;

public class User {
    private ArrayList<Achievement> achievements;
    private ArrayList<Book> library;
    private ArrayList<Genre> genres;
    private ArrayList<Book> interested;
    private ArrayList<Book> read;
    private Boolean premium;
    private String picture;
    private String name;

    public User(String name, String picture){
        this.name = name;
        this.picture = picture;
    }

    public User() {
        library = new ArrayList<>();
        genres = new ArrayList<>();
        premium = false;
        achievements = new ArrayList<>();
        interested = new ArrayList<>();
        read = new ArrayList<>();
        this.name = "Oscar R";
        this.picture = "userfinale";
    }

    public User(String name, Boolean premium, ArrayList<Genre> genres, ArrayList<Book> library) {
        this.name = name;
        this.premium = premium;
        if (library != null)
            this.library = library;
        if (genres != null)
            this.genres = genres;
        this.achievements = MockupsValues.getAchievementsPersonalized();
    }

    public Boolean isPremium() { return premium; }

    public void setPremium(Boolean premium) { this.premium = premium; }

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

    public void addBookToInterestedBooks(Book book){
        interested.add(book);
    }

    public ArrayList<Book> getReadBooks(){
        return read;
    }

    public void addBookToReadBooks(Book book){
        read.add(book);
    }

    public ArrayList<Book> getInterestedBooks(){
        return interested;
    }

    public ArrayList<Genre> getGenres() { return genres; }

    public void setGenres(ArrayList<Genre> genres) { this.genres = genres; }

    public void addGenreToGenres(Genre genre){ genres.add(genre); }

    public void removeGenreToGenres(Genre genre){
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

    public ArrayList<Achievement> getAchievements() {
        return achievements;
    }

    public void setAchievements(ArrayList<Achievement> achievements) {
        this.achievements = achievements;
    }

    public int getNumCompletedAchievements() {
        int numCompleted = 0;
        ListIterator<Achievement> iterator = this.achievements.listIterator();
        while (iterator.hasNext()) {
            if (iterator.next().isCompleted())
                numCompleted++;
        }
        return numCompleted;
    }

    public ArrayList<Achievement> getCompletedAchievements() {
        ArrayList<Achievement> results = new ArrayList<>();
        Achievement achievement;

        ListIterator<Achievement> iterator = this.achievements.listIterator();
        while (iterator.hasNext()) {
            achievement = iterator.next();
            if (achievement.isCompleted())
                results.add(achievement);
        }

        return results;

    public void booksReSet(){
        for (Book book : getLibrary()){
            if(book.isRead()){
                addBookToReadBooks(book);
            } else {
                addBookToInterestedBooks(book);
            }
        }
    }
}
