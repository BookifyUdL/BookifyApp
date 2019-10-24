package com.example.readify.Popups;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readify.Adapters.BooksListVerticalAdapter;
import com.example.readify.MainActivity;
import com.example.readify.MockupsValues;
import com.example.readify.Models.Book;
import com.example.readify.Models.Genre;
import com.example.readify.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class BookReadedPopup extends DialogFragment {

    private View view;
    private ArrayList<CardView> stars;
    private ArrayList<ImageButton> starsImage;
    private FragmentManager fragmentManager;
    private int width, height;

    public  BookReadedPopup(FragmentManager fragmentManager){
        this.fragmentManager = fragmentManager;
        //this.width = width;
        //this.height = height;
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int index = Integer.parseInt(view.getTag().toString());
            for (int i = 0; i < stars.size(); i++){
                if(i < index){
                    //stars.get(i).setBackgroundDrawable();
                    Drawable drawable = ContextCompat.getDrawable(getContext(),
                            getContext().getResources().getIdentifier("ic_star", "drawable", getContext().getPackageName()));
                    starsImage.get(i).setImageDrawable(drawable);
                } else {
                    Drawable drawable = ContextCompat.getDrawable(getContext(),
                            getContext().getResources().getIdentifier("ic_empty_star", "drawable", getContext().getPackageName()));
                    starsImage.get(i).setImageDrawable(drawable);
                }
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.book_readed_popup, container);
        CardView cardView = view.findViewById(R.id.card_reviews);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReviewsPopup dialog =  new ReviewsPopup();
                //dialog = new ProfileDialog(post.getGuide(), this, post.getPlace());
                FragmentTransaction ft2 = fragmentManager.beginTransaction();
                dialog.show(ft2, "reviews_fragment");
            }
        });
        //LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.top_linear_layout);
        //RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.top_relative_layout);
        //linearLayout.setBackgroundColor(Color.TRANSPARENT);
        //relativeLayout.setBackgroundColor(Color.TRANSPARENT);


        CardView star1 = (CardView) view.findViewById(R.id.card_view11);
        CardView star2 = (CardView) view.findViewById(R.id.card_view12);
        CardView star3 = (CardView) view.findViewById(R.id.card_view13);
        CardView star4 = (CardView) view.findViewById(R.id.card_view14);
        CardView star5 = (CardView) view.findViewById(R.id.card_view15);
        ImageButton starButton1 = (ImageButton) view.findViewById(R.id.star1);
        ImageButton starButton2 = (ImageButton) view.findViewById(R.id.star2);
        ImageButton starButton3 = (ImageButton) view.findViewById(R.id.star3);
        ImageButton starButton4 = (ImageButton) view.findViewById(R.id.star4);
        ImageButton starButton5 = (ImageButton) view.findViewById(R.id.star5);
        starButton1.setOnClickListener(listener);
        starButton2.setOnClickListener(listener);
        starButton3.setOnClickListener(listener);
        starButton4.setOnClickListener(listener);
        starButton5.setOnClickListener(listener);
        stars = new ArrayList<>();
        starsImage = new ArrayList<>();
        stars.add(star1);
        stars.add(star2);
        stars.add(star3);
        stars.add(star4);
        stars.add(star5);
        starsImage.add(starButton1);
        starsImage.add(starButton2);
        starsImage.add(starButton3);
        starsImage.add(starButton4);
        starsImage.add(starButton5);


        /*LinearLayoutManager vlm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.comments_recyclerView);
        recyclerView.setLayoutManager(vlm);
        ArrayList<Book> pendingBooksList = new ArrayList<>();
        pendingBooksList.addAll(MockupsValues.getLastAddedBooks());
        BooksListVerticalAdapter pendingBooksAdapter = new BooksListVerticalAdapter((MainActivity) getActivity(), getContext(), pendingBooksList);
        recyclerView.setAdapter(pendingBooksAdapter);*/

        return view;
    }
}
