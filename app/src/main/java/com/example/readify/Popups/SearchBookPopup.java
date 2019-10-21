package com.example.readify.Popups;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readify.Adapters.BooksListVerticalAdapter;
import com.example.readify.MockupsValues;
import com.example.readify.R;

import java.util.ArrayList;

public class SearchBookPopup extends DialogFragment {

    View view;
    int width, height;

    public  SearchBookPopup(int width, int height){
        this.width = width;
        this.height = height;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.search_books_popup, container);
        /*RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.books_recycler_view);
        BooksListVerticalAdapter adapter = new BooksListVerticalAdapter(getContext(), MockupsValues.getLastAddedBooks());
        recyclerView.setAdapter(adapter);*/
        LinearLayoutManager verticalLayoutManagaer = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        //verticalLayoutManagaer.wid
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.books_recycler_view);
        recyclerView.setLayoutManager(verticalLayoutManagaer);
        ArrayList list = MockupsValues.getLastAddedBooks();
        list.remove(list.size() - 1);
        BooksListVerticalAdapter adapter = new BooksListVerticalAdapter(getContext(), list);
        recyclerView.setAdapter(adapter);
        //view.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(width, height);
        /*params.height = height;
        params.width = width;*/
        view.setLayoutParams(params);
        return view;
    }
}
