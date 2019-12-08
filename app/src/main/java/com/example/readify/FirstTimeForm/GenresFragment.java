package com.example.readify.FirstTimeForm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.readify.Adapters.BooksListFormAdapter;
import com.example.readify.Adapters.BooksListVerticalAdapter;
import com.example.readify.MockupsValues;
import com.example.readify.Models.Achievement;
import com.example.readify.Models.Book;
import com.example.readify.Models.Genre;
import com.example.readify.Models.User;
import com.example.readify.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.CirclePageIndicator;
import com.synnapps.carouselview.ViewListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GenresFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GenresFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GenresFragment extends Fragment implements RecyclerViewAdapterGenres.ItemClickListener, BooksListFormAdapter.ItemClickListener, SearchView.OnQueryTextListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private View view;
    private Activity activity;
    private ComunicateFragmentsFirstForm comunicateFragmentsFirstForm;

    //CarouselView
    private CarouselView carouselView;
    private int NUMBER_OF_FORMS = 3;

    private RecyclerViewAdapterGenres adapterGenres;
    private BooksListFormAdapter adapterBooksList;
    private BooksListFormAdapter adapterBooksInterest;
    private SharedPreferences pref;

    private User user;
    //User user = MockupsValues.getUser();

    public GenresFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GenresFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GenresFragment newInstance(String param1, String param2) {
        GenresFragment fragment = new GenresFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_genres, container, false);
        pref = getContext().getSharedPreferences("com.example.readify", Context.MODE_PRIVATE);

        //Create a new user with the information about the facebook or google
        user = new User();
        user.setUid(pref.getString("com.example.readify.uid", "0"));
        user.setName(pref.getString("com.example.readify.name", "Unknown"));
        user.setEmail(pref.getString("com.example.readify.email", "email@unknown.com"));
        user.setPicture(pref.getString("com.example.readify.photo", ""));
        user.setPremium(false);

        //Initialize carouselView
        CirclePageIndicator indicator = view.findViewById(R.id.indicator);
        indicator.setFillColor(R.color.default_circle_indicator_stroke_color);

        carouselView = (CarouselView) view.findViewById(R.id.carouselView);
        carouselView.setPageCount(NUMBER_OF_FORMS);
        carouselView.setSlideInterval(0);
        carouselView.setViewListener(viewListener);
        carouselView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                if (position == 2)
                    changeInterestedBooks();
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        // Inflate the layout for this fragment
        return view;
    }

    ViewListener viewListener = new ViewListener() {

        @Override
        public View setViewForPosition(int position) {
            View view = null;

            switch (position) {
                case 0:
                    //Selección de géneros
                    view = getLayoutInflater().inflate(R.layout.first_form_genres, null);
                    FloatingActionButton next = view.findViewById(R.id.next);
                    next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            goNextCarousselItem();
                        }
                    });
                    createBookGenresDinamically(view);
                    break;

                case 1:
                    //Libros leídos
                    view = getLayoutInflater().inflate(R.layout.first_form_books_read, null);
                    FloatingActionButton next2 = view.findViewById(R.id.next);
                    next2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            goNextCarousselItem();
                        }
                    });
                    createBookReadDinamically(view);
                    break;

                case 2:
                    //Libros por interés
                    view = getLayoutInflater().inflate(R.layout.first_form_books_interest, null);
                    FloatingActionButton done = view.findViewById(R.id.next);
                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showTutorialDonePopup();
                        }
                    });
                    createBookInterestDinamically(view);
                    break;

                /*case 3:
                    //Finalización del formulario
                    view = getLayoutInflater().inflate(R.layout.first_form_done, null);
                    Button buttonDone = view.findViewById(R.id.buttonFirstFormDone);
                    buttonDone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            pref.edit().putBoolean("com.example.readify.premium", user.isPremium()).apply();

                            String genresToPref = new Gson().toJson(user.getGenres());
                            pref.edit().putString("com.example.readify.genres", genresToPref).apply();

                            String libraryToPref = new Gson().toJson(user.getLibrary());
                            pref.edit().putString("com.example.readify.library", libraryToPref).apply();

                            String interestedToPref = new Gson().toJson(user.getInterested());
                            pref.edit().putString("com.example.readify.interested", interestedToPref).apply();

                            String achievementsToPref = new Gson().toJson(MockupsValues.getAchievements());
                            pref.edit().putString("com.example.readify.achievements", achievementsToPref).apply();

                            user.setAchievements(MockupsValues.getAchievements());

                            comunicateFragmentsFirstForm.doneForm(user);
                        }
                    });*/
            }

            //Defining a skip button
            TextView skipForm = view.findViewById(R.id.textSkipFirstForm);
            if (skipForm != null) {
                skipForm.setText(R.string.skip_first_form_message);
                skipForm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pref.edit().putBoolean("com.example.readify.premium", user.isPremium()).apply();

                        String genresToPref = new Gson().toJson(user.getGenres());
                        pref.edit().putString("com.example.readify.genres", genresToPref).apply();

                        String libraryToPref = new Gson().toJson(user.getLibrary());
                        pref.edit().putString("com.example.readify.library", libraryToPref).apply();

                        String interestedToPref = new Gson().toJson(user.getInterested());
                        pref.edit().putString("com.example.readify.interested", interestedToPref).apply();

                        String achievementsToPref = new Gson().toJson(MockupsValues.getAchievements());
                        pref.edit().putString("com.example.readify.achievements", achievementsToPref).apply();

                        user.setAchievements(MockupsValues.getAchievements());

                        comunicateFragmentsFirstForm.exitForm(user);
                    }
                });
            }

            return view;
        }
    };

    private void showTutorialDonePopup(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());

        String message = getString(R.string.done_first_form_text) + "\n\n" + getString(R.string.done_redirect_first_form_text);
        builder1.setMessage(message);
        builder1.setCancelable(false);

        builder1.setTitle(getString(R.string.done_first_form_title));

        builder1.setPositiveButton(
                getString(R.string.done_end),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        pref.edit().putBoolean("com.example.readify.premium", user.isPremium()).apply();

                        String genresToPref = new Gson().toJson(user.getGenres());
                        pref.edit().putString("com.example.readify.genres", genresToPref).apply();

                        String libraryToPref = new Gson().toJson(user.getLibrary());
                        pref.edit().putString("com.example.readify.library", libraryToPref).apply();

                        String interestedToPref = new Gson().toJson(user.getInterested());
                        pref.edit().putString("com.example.readify.interested", interestedToPref).apply();

                        String achievementsToPref = new Gson().toJson(MockupsValues.getAchievements());
                        pref.edit().putString("com.example.readify.achievements", achievementsToPref).apply();

                        user.setAchievements(MockupsValues.getAchievements());

                        dialog.cancel();
                        comunicateFragmentsFirstForm.doneForm(user);
                    }
                });

        /*builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });*/

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void goNextCarousselItem(){
        carouselView.setCurrentItem(carouselView.getCurrentItem() + 1);
    }

    private void createBookGenresDinamically(View view) {
        //Defining every genre
        RecyclerView recyclerViewGenres = view.findViewById(R.id.recyclerViewGenres);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 3);
        recyclerViewGenres.setLayoutManager(gridLayoutManager);

        ArrayList genres = MockupsValues.getGenres();

        adapterGenres = new RecyclerViewAdapterGenres(getContext(), genres, user);
        adapterGenres.setClickListener(GenresFragment.this);
        recyclerViewGenres.setAdapter(adapterGenres);
    }

    private void createBookReadDinamically(View view) {
        //Defining the adapters and SearchView to show every book
        RecyclerView recyclerViewReadBooks = view.findViewById(R.id.recyclerViewReadBooks);
        GridLayoutManager gridLayoutManagerBooksRead = new GridLayoutManager(view.getContext(), 1);
        //LinearLayoutManager linearLayoutManagerRead = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewReadBooks.setLayoutManager(gridLayoutManagerBooksRead);

        ArrayList booksRead = MockupsValues.getAllBooksForTutorial();

        adapterBooksList = new BooksListFormAdapter(getContext(), booksRead, user, true);
        adapterBooksList.setClickListener(GenresFragment.this);
        recyclerViewReadBooks.setAdapter(adapterBooksList);

        SearchView searchViewBooksRead = view.findViewById(R.id.search_bar_read_books);
        searchViewBooksRead.setFocusable(false);
        searchViewBooksRead.clearFocus();
        searchViewBooksRead.setOnQueryTextListener(GenresFragment.this);
    }

    private void changeInterestedBooks() {
        ArrayList booksInterest = MockupsValues.getAllBooksForTutorial();
        for (Book book : user.getLibrary()) {
            booksInterest.remove(book);
        }
        adapterBooksInterest.setBooksList(booksInterest);
        adapterBooksInterest.notifyDataSetChanged();
    }

    private void createBookInterestDinamically(View view) {
        //Defining the adapters and SearchView to show every book
        LinearLayoutManager linearLayoutManagerInterest = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerViewInterestBooks = view.findViewById(R.id.recyclerViewInterestBooks);
        recyclerViewInterestBooks.setLayoutManager(linearLayoutManagerInterest);

        ArrayList booksInterest = MockupsValues.getAllBooksForTutorial();

        //ArrayList booksInterest = MockupsValues.getLastAddedBooks();
        adapterBooksInterest = new BooksListFormAdapter(getContext(), booksInterest, user, false);
        adapterBooksInterest.setClickListener(GenresFragment.this);
        recyclerViewInterestBooks.setAdapter(adapterBooksInterest);

        SearchView searchViewBooksInterest = view.findViewById(R.id.search_bar_interest_books);
        searchViewBooksInterest.setFocusable(false);
        searchViewBooksInterest.clearFocus();
        searchViewBooksInterest.setOnQueryTextListener(GenresFragment.this);
    }

    @Override
    public void onItemClick(View view) {
        switch (carouselView.getCurrentItem()) {
            case 0:
                LinearLayout linearLayoutGenre = view.findViewById(R.id.LayoutBackgroundCardView);
                CardView cardViewGenre = view.findViewById(R.id.cardView);
                changeCardView(cardViewGenre, linearLayoutGenre);
                break;
            case 1:
            case 2:
                LinearLayout linearLayoutCardViewInterestBook = view.findViewById(R.id.linearLayoutCardView);
                CardView cardViewInterestBook = view.findViewById(R.id.card_view);
                changeCardView(cardViewInterestBook, linearLayoutCardViewInterestBook);
                break;
        }
    }

    void changeCardView(CardView cardView, LinearLayout linearLayout) {
        if (cardView.getTag() == getString(R.string.cardView_unmark)) {
            cardView.setTag(getString(R.string.cardView_mark));
            linearLayout.setBackground(view.getContext().getDrawable(R.drawable.cardview_selected));
        } else {
            cardView.setTag(getString(R.string.cardView_unmark));
            linearLayout.setBackgroundColor(view.getContext().getColor(R.color.colorGrayCardBackground));
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            activity = (Activity) context;
            comunicateFragmentsFirstForm = (ComunicateFragmentsFirstForm) activity;
        }
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        switch (carouselView.getCurrentItem()) {
            case 1:
                adapterBooksList.filter(s);
                break;
            case 2:
                adapterBooksInterest.filter(s);
                break;
        }
        return false;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
