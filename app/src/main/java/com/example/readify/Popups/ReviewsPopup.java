package com.example.readify.Popups;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readify.Adapters.BooksListVerticalAdapter;
import com.example.readify.MainActivity;
import com.example.readify.MockupsValues;
import com.example.readify.Models.Book;
import com.example.readify.R;

import java.util.ArrayList;

public class ReviewsPopup extends DialogFragment implements Popup {

    public ReviewsPopup(){

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.reviews_layout, container);
        LinearLayoutManager vlm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.comments_recyclerView);
        recyclerView.setLayoutManager(vlm);
        ArrayList<Book> pendingBooksList = new ArrayList<>();
        pendingBooksList.addAll(MockupsValues.getLastAddedBooks());
        BooksListVerticalAdapter pendingBooksAdapter = new BooksListVerticalAdapter((MainActivity) getActivity(), getContext(), pendingBooksList);
        recyclerView.setAdapter(pendingBooksAdapter);
        ImageButton closeArrow = (ImageButton) view.findViewById(R.id.close_arrow);
        closeArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
            }
        });
        //this.dismiss();
        return view;
    }

    public void close(){
        this.dismiss();
    }
}
