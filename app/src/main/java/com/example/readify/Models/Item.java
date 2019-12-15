package com.example.readify.Models;

import com.example.readify.MockupsValues;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

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

    public Item(JSONObject itemJson){
        try {
            /*
            "shop_id": [
                {
                    "_id": "5de7fb5a5a66a02fe3c39eb6",
                    "url": "https://www.elcorteingles.es/recursos/informacioncorporativa/img/portal/2017/07/06/el-corte-ingles-triangulo.png",
                    "name": "El Corte Ingles"
                }
            ],
            "_id": "5de7fb665a66a02fe3c39ebe",
            "url": "elcorteingles.es/libros/A33030295-incondicional-edicion-limitada-firmada-tapa-dura-2065212770194/",
            "price": "15.1",
            * */
            this.id = itemJson.getString("_id");
            this.price = Double.parseDouble(itemJson.getString("price"));
            this.url = itemJson.getString("url");
            JSONArray jsonArray = itemJson.getJSONArray("shop_id");
            Shop aux = new Shop(jsonArray.getJSONObject(0));
            this.shop = MockupsValues.getCorrespondiShop(aux);

        } catch (Exception e){
            System.out.println("Error parsing item");
        }
    }

    public static ArrayList<Item> getItemsFromJSONArray(JSONArray jsonArray){
        ArrayList<Item> items = new ArrayList<>();
        for (int i=0; i < jsonArray.length(); i++){
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                items.add(new Item(jsonObject));
            } catch (Exception e) {
                System.out.println("Error parsing in method: getItemsFromJSONArray");
            }

        }
        return items;
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
