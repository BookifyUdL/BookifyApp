package com.example.readify.Popups;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readify.Adapters.BooksListVerticalAdapter;
import com.example.readify.Adapters.EmojisAdapter;
import com.example.readify.ApiConnector;
import com.example.readify.MainActivity;
import com.example.readify.MockupsValues;
import com.example.readify.Models.Book;
import com.example.readify.Models.Emoji;
import com.example.readify.Models.ServerCallback;
import com.example.readify.Models.User;
import com.example.readify.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.ListIterator;

public class BookReadedPopup extends DialogFragment implements Popup {

    private View view;
    private ArrayList<CardView> stars;
    private ArrayList<ImageButton> starsImage;
    private FragmentManager fragmentManager;
    private Book book;
    private MainActivity activity;
    private BooksListVerticalAdapter.BookHolder bookHolder;
    private ArrayList<Emoji> emojis;
    private int starsClicked;

    private User user;
    private SharedPreferences pref;

    public  BookReadedPopup(MainActivity activity, BooksListVerticalAdapter.BookHolder bookHolder, FragmentManager fragmentManager, Book book, User user){
        this.fragmentManager = fragmentManager;
        this.activity = activity;
        this.book = book;
        this.bookHolder = bookHolder;
        this.user = user;
        this.starsClicked = 0;
        //this.emojis = em
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            int index = Integer.parseInt(view.getTag().toString());
            starsClicked = index;

            for (int i = 0; i < stars.size(); i++){
                if(i < index){
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
        ReviewsPopup dialog =  new ReviewsPopup(this.book);
        FragmentTransaction ft2 = fragmentManager.beginTransaction();
        dialog.show(ft2, "reviews_fragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.book_readed_popup, container);

        pref = view.getContext().getSharedPreferences("com.example.readify", Context.MODE_PRIVATE);

        ImageView cover = (ImageView) view.findViewById(R.id.book_cover);
        setBookCover(cover, book.getPicture());

        this.getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        CardView cardView = view.findViewById(R.id.card_reviews);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showReviewsPopup();
            }
        });

        TextView bookTitle = (TextView) view.findViewById(R.id.info_text);
        bookTitle.setText(book.getTitle());

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
        stars.add(star1);
        stars.add(star2);
        stars.add(star3);
        stars.add(star4);
        stars.add(star5);

        starsImage = new ArrayList<>();
        starsImage.add(starButton1);
        starsImage.add(starButton2);
        starsImage.add(starButton3);
        starsImage.add(starButton4);
        starsImage.add(starButton5);

        RecyclerView recyclerViewGenres = view.findViewById(R.id.recylcer_view_emojis);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 4);
        recyclerViewGenres.setLayoutManager(gridLayoutManager);

        //ArrayList<Emoji> emojis = MockupsValues.getEmojis();
        //ArrayList<Emoji> emojis = book.getEmojis();

        EmojisAdapter emojisAdapter = new EmojisAdapter(getContext(), book.getEmojis());
        recyclerViewGenres.setAdapter(emojisAdapter);



        return view;
    }

    private void setBookCover(ImageView holder, String picture){
        Picasso.with(getContext()) // Context
                .load(picture) // URL or file
                .into(holder);
    }

    private void acceptButtonClicked(){
        //ArrayList<Book> library = user.getLibrary();
        ArrayList<Book> reading = user.getReading();
        reading.remove(book);
        ArrayList<Book> library = user.getLibrary();
        library.remove(book);

        book.setRead(true);
        book.setSumRatings(book.getSumRatings() + starsClicked);
        book.setNumRatings(book.getNumRatings() + 1);

        //MockupsValues.user.setReading(reading);



        ArrayList<Book> auxLibrary = new ArrayList<>();
        auxLibrary.add(book);
        auxLibrary.addAll(library);
        //library.add(0, book);

        //MockupsValues.user.setLibrary(library);

        user.setReading(reading);
        user.setLibrary(auxLibrary);
        MockupsValues.user = user;
        //MockupsValues.removeReadingListBook(book);
        //String readingToPref = new Gson().toJson(user.getReading());
        //pref.edit().putString("com.example.readify.reading", readingToPref).apply();
        ApiConnector.updateBook(getContext(), book, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                Toast.makeText(getContext(), getContext().getString(R.string.review_added_correctly), Toast.LENGTH_LONG).show();
                close();
                bookHolder.destroyView();
            }
        });
        ApiConnector.updateUser(getContext(), new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result) {

            }
        }, MockupsValues.user);
    }

    public void close(){
        this.dismiss();
    }

    @Override
    public void onResume() {
        super.onResume();
        //float dim = ViewGroup.LayoutParams.WRAP_CONTENT - getContext().getResources().getDimension(R.dimen.margin_10dp);
        Window window = getDialog().getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.CENTER);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        this.activity.notifyReadingListChanged(user);
        this.activity.notifyLibraryListChanged(user, true);
    }
}
