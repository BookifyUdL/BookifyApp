package com.example.readify.Popups;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readify.Adapters.BooksListVerticalAdapter;
import com.example.readify.MockupsValues;
import com.example.readify.Models.Book;
import com.example.readify.Models.User;
import com.example.readify.R;

import java.util.ArrayList;

public class SearchBookPopup extends DialogFragment {

    View view;
    int width, height;

    private SharedPreferences prefs;
    private User user;

    public  SearchBookPopup(int width, int height){
        this.width = width;
        this.height = height;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.search_books_popup, container);

        prefs = view.getContext().getSharedPreferences("com.example.readify", Context.MODE_PRIVATE);
        user = new User();
        user.readFromSharedPreferences(prefs);

        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.books_recycler_view);
        recyclerView.setLayoutManager(verticalLayoutManager);
        //ArrayList list = MockupsValues.getLastAddedBooks();
        ArrayList<Book> list = user.getLibrary();
        list.remove(list.size() - 1);

        BooksListVerticalAdapter adapter = new BooksListVerticalAdapter(getContext(), list, user);
        recyclerView.setAdapter(adapter);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(width, height);
        view.setLayoutParams(params);

        return view;
    }
}
