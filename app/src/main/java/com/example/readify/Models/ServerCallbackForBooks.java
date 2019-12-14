package com.example.readify.Models;

import org.json.JSONObject;

import java.util.ArrayList;

public interface ServerCallbackForBooks {
    abstract void onSuccess(ArrayList<ArrayList<Book>> books);
    abstract void onSuccess(Book book);
}
