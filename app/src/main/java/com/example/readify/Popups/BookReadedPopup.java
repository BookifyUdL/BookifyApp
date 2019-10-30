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
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readify.Adapters.BooksGridAdapter;
import com.example.readify.Adapters.BooksListVerticalAdapter;
import com.example.readify.Adapters.EmojisAdapter;
import com.example.readify.MainActivity;
import com.example.readify.MockupsValues;
import com.example.readify.Models.Book;
import com.example.readify.Models.Emoji;
import com.example.readify.Models.Genre;
import com.example.readify.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class BookReadedPopup extends DialogFragment implements Popup {

    private View view;
    private ArrayList<CardView> stars;
    private ArrayList<ImageButton> starsImage;
    private FragmentManager fragmentManager;
    private Book book;
    private MainActivity activity;
    private BooksListVerticalAdapter.BookHolder bookHolder;

    public  BookReadedPopup(MainActivity activity, BooksListVerticalAdapter.BookHolder bookHolder, FragmentManager fragmentManager, Book book){
        this.fragmentManager = fragmentManager;
        this.activity = activity;
        this.book = book;
        this.bookHolder = bookHolder;
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

    private void showReviewsPopup(){
        ReviewsPopup dialog =  new ReviewsPopup();
        FragmentTransaction ft2 = fragmentManager.beginTransaction();
        dialog.show(ft2, "reviews_fragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.book_readed_popup, container);
        CardView cardView = view.findViewById(R.id.card_reviews);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showReviewsPopup();
            }
        });

        ImageButton imageButton = (ImageButton) view.findViewById(R.id.reviews_image_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showReviewsPopup();
            }
        });

        ImageButton closeArrow = (ImageButton) view.findViewById(R.id.close_arrow);
        closeArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
            }
        });

        CardView cancelCardView = view.findViewById(R.id.cancel_card_view);
        cancelCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
            }
        });

        TextView cancelTextView = view.findViewById(R.id.cancel_text_view);
        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
            }
        });

        CardView acceptCardView = view.findViewById(R.id.accept_card_view);
        acceptCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptButtonClicked();
            }
        });

        TextView acceptTextView = view.findViewById(R.id.accept_text_view);
        acceptTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptButtonClicked();
            }
        });


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

        RecyclerView recyclerViewGenres = view.findViewById(R.id.recylcer_view_emojis);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 4);
        recyclerViewGenres.setLayoutManager(gridLayoutManager);

        ArrayList<Emoji> emojis = MockupsValues.getEmojis();

        EmojisAdapter emojisAdapter = new EmojisAdapter(getContext(), emojis);
        recyclerViewGenres.setAdapter(emojisAdapter);

        return view;
    }

    private void acceptButtonClicked(){
        MockupsValues.user.setLibraryBookAsRead(book);
        this.activity.notifyLibraryListChanged();
        MockupsValues.removeReadingListBook(book);
        this.activity.notifyReadingListChanged();
        Toast.makeText(getContext(), getContext().getString(R.string.review_added_correctly), Toast.LENGTH_LONG).show();
        close();
        bookHolder.destroyView();
    }

    public void close(){
        this.dismiss();
    }
}
