package com.example.readify.Models;

public class Emoji {

    private String name;
    private String emoji;
    private int value;
    private int num;

    public Emoji() {}

    public Emoji(String name, String emoji) {
        this.name = name;
        this.emoji = emoji;
    }

    public Emoji(String name, String emoji, int value, int num){
        this.name = name;
        this.emoji = emoji;
        this.value = value;
        this.num = num;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }
}
