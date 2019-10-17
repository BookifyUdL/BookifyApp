package com.example.readify.Models;

public class Book {
    private String title;
    private String author;
    private String picture;
    private int year;

    public Book(String title, String author, String picture, int year) {
        this.title = title;
        this.author = author;
        this.picture = picture;
        this.year = year;
    }

    public Book(String title, String author, String picture) {
        this.title = title;
        this.author = author;
        this.picture = picture;
    }

    public Book(){ }

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
}
