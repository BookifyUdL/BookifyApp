package com.example.readify.Models;

public class Book {
    private String title;
    private String author;
    private String picture;
    private boolean read;
    private int year;
    private int extension;
    private Genre genre;

    public Book(String title, String author, String picture, int year, int extension, Genre genre) {
        this.title = title;
        this.author = author;
        this.picture = picture;
        this.year = year;
        this.read = false;
        this.genre = genre;
        this.extension = extension;
    }

    public Book(String title, String author, String picture, int year) {
        this.title = title;
        this.author = author;
        this.picture = picture;
        this.year = year;
        this.read = false;
    }

    public Book(String title, String author, String picture) {
        this.title = title;
        this.author = author;
        this.picture = picture;
        this.read = false;
    }

    public Book() {
    }

    public int getExtension() { return this.extension; }

    public void setExtension(int extension){
        this.extension = extension;
    }

    public Genre getGenre() { return this.genre; }

    public void setGenre(Genre genre) { this.genre = genre; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
