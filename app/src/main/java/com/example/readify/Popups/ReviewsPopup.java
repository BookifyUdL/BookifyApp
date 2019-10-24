package com.example.readify.Popups;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;

import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readify.Adapters.BooksListVerticalAdapter;
import com.example.readify.Adapters.ReviewsVerticalAdapter;
import com.example.readify.MainActivity;
import com.example.readify.MockupsValues;
import com.example.readify.Models.Book;
import com.example.readify.Models.Review;
import com.example.readify.R;

import java.util.ArrayList;

public class ReviewsPopup extends DialogFragment implements Popup {

    public ReviewsPopup(){

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.reviews_layout, container);
        //setWindow();
        //(this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //getDialog().getWindow().setLayout(500, 500);
        LinearLayoutManager vlm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.comments_recyclerView);
        recyclerView.setLayoutManager(vlm);
        ArrayList<Review> pendingBooksList = new ArrayList<>();
        pendingBooksList.addAll(MockupsValues.getReviews());
        ReviewsVerticalAdapter pendingBooksAdapter = new ReviewsVerticalAdapter((MainActivity) getActivity(), getContext(), pendingBooksList);
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

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.CENTER);
        //TODO:
    }

    public void setWindow(){
        this.getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    /*publicgetWindow(){
        this.getWindow();
    }*/

    public void close(){
        this.dismiss();
    }
}
