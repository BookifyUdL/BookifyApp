package com.example.readify.Models;

import java.util.ArrayList;

public class Achievement {
    private int id;
    private String title;
    private String description;
    private String image;
    private int progressValue;
    private int totalValue;
    //private Genre genere;
    //private ArrayList<Genre> genres;

    public Achievement(int id, String title, String description, String image, int progressValue, int totalValue) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.image = image;
        this.progressValue = progressValue;
        this.totalValue = totalValue;
    }

    public Achievement(){}

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getProgressValue() {
        return progressValue;
    }

    public void setProgressValue(int progressValue) {
        this.progressValue = progressValue;
    }

    public int getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(int totalValue) {
        this.totalValue = totalValue;
    }

    public boolean isCompleted() {
        return progressValue == totalValue;
    }

    public void incrementValue(int value) {
        this.progressValue += value;
        if (progressValue > totalValue)
            progressValue = totalValue;
    }
}
