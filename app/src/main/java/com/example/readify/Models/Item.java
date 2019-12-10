package com.example.readify.Models;

public class Item implements Comparable {

    private String id;
    private String url;
    private double price;
    private String book_id;
    private String shop_id;
    private Shop shop;

    public Item(String id, String url, double price, String book_id, Shop shop) {
        this.id = id;
        this.url = url;
        this.price = price;
        this.book_id = book_id;
        this.shop = shop;
    }

    public Item(String id, String url, double price, String book_id, String shop_id) {
        this.id = id;
        this.url = url;
        this.price = price;
        this.book_id = book_id;
        this.shop_id = shop_id;
    }

    public Item(double price, Shop shop){
        this.price = price;
        this.shop = shop;
    }

    @Override
    public int compareTo(Object o) {
        Item compareItem = (Item) o;
        if(this.getPrice() < compareItem.getPrice())
            return -1;
        else if (this.getPrice() > compareItem.getPrice())
            return 1;
        return 0;
    }

    @Override
    public String toString() {
        return this.getShop().getName();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

}
