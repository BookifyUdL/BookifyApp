package com.example.readify.Models;

import org.json.JSONObject;

import java.util.ArrayList;

public abstract interface ServerCallbackForBooks {
    void onSuccess(ArrayList<ArrayList<Book>> books);
    void onSuccess(Book book);
}
