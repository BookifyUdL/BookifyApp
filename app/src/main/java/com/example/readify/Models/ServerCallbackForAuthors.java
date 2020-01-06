package com.example.readify.Models;

import java.util.ArrayList;

public interface ServerCallbackForAuthors {
    void onSuccess(ArrayList<ArrayList<Author>> authors);
    void onSuccess(Author author);
}
