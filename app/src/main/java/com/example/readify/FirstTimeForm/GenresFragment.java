package com.example.readify.FirstTimeForm;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.readify.Adapters.BooksListVerticalAdapter;
import com.example.readify.MockupsValues;
import com.example.readify.Models.User;
import com.example.readify.R;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.CirclePageIndicator;
import com.synnapps.carouselview.ViewListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GenresFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GenresFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GenresFragment extends Fragment implements RecyclerViewAdapterGenres.ItemClickListener, SearchView.OnQueryTextListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    View view;
    Activity activity;
    ComunicateFragmentsFirstForm comunicateFragmentsFirstForm;

    //CarouselView
    CarouselView carouselView;
    int NUMBER_OF_FORMS = 4;

    RecyclerViewAdapterGenres adapterGenres;
    BooksListVerticalAdapter adapterBooksList;

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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_genres, container, false);
        CirclePageIndicator indicator = view.findViewById(R.id.indicator);
        indicator.setFillColor(R.color.default_circle_indicator_stroke_color);

        carouselView = (CarouselView) view.findViewById(R.id.carouselView);
        carouselView.setPageCount(NUMBER_OF_FORMS);
        carouselView.setSlideInterval(0);
        carouselView.setViewListener(viewListener);

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

                    //Defining every genre
                    RecyclerView recyclerViewGenres = view.findViewById(R.id.recyclerViewGenres);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 3);
                    recyclerViewGenres.setLayoutManager(gridLayoutManager);

                    ArrayList genres = MockupsValues.getGenres();

                    adapterGenres = new RecyclerViewAdapterGenres(getContext(), genres);
                    adapterGenres.setClickListener(GenresFragment.this);
                    recyclerViewGenres.setAdapter(adapterGenres);
                    break;

                case 1:
                    //Libros leídos
                    view = getLayoutInflater().inflate(R.layout.first_form_books_read, null);

                    LinearLayoutManager linearLayoutManagerRead = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
                    RecyclerView recyclerViewReadBooks = view.findViewById(R.id.recyclerViewReadBooks);
                    recyclerViewReadBooks.setLayoutManager(linearLayoutManagerRead);

                    ArrayList booksRead = MockupsValues.getLastAddedBooks();
                    adapterBooksList = new BooksListVerticalAdapter(getContext(), booksRead, new User());
                    recyclerViewReadBooks.setAdapter(adapterBooksList);

                    SearchView searchViewBooksRead = view.findViewById(R.id.search_bar_read_books);
                    searchViewBooksRead.setFocusable(false);
                    searchViewBooksRead.clearFocus();
                    searchViewBooksRead.setOnQueryTextListener(GenresFragment.this);

                    break;

                case 2:
                    //Libros por interés
                    view = getLayoutInflater().inflate(R.layout.first_form_books_interest, null);

                    LinearLayoutManager linearLayoutManagerInterest = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
                    RecyclerView recyclerViewInterestBooks = view.findViewById(R.id.recyclerViewInterestBooks);
                    recyclerViewInterestBooks.setLayoutManager(linearLayoutManagerInterest);

                    ArrayList booksInterest = MockupsValues.getLastAddedBooks();
                    //TODO Cambiar el adapter perque sino un dels dos buscadors no funciona
                    adapterBooksList = new BooksListVerticalAdapter(getContext(), booksInterest, new User());
                    recyclerViewInterestBooks.setAdapter(adapterBooksList);

                    SearchView searchViewBooksInterest = view.findViewById(R.id.search_bar_interest_books);
                    searchViewBooksInterest.setFocusable(false);
                    searchViewBooksInterest.clearFocus();
                    searchViewBooksInterest.setOnQueryTextListener(GenresFragment.this);
                    break;

                case 3:
                    //Finalización del formulario
                    view = getLayoutInflater().inflate(R.layout.first_form_done, null);
                    Button buttonDone = view.findViewById(R.id.buttonFirstFormDone);
                    buttonDone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            comunicateFragmentsFirstForm.doneForm();
                        }
                    });
            }

            //Defining a skip button
            TextView skipForm = view.findViewById(R.id.textSkipFirstForm);
            if (skipForm != null) {
                skipForm.setText(R.string.skip_first_form_message);
                skipForm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        comunicateFragmentsFirstForm.exitForm();
                    }
                });
            }

            return view;
        }
    };

    @Override
    public void onItemClick(View view, int position) {
        LinearLayout linearLayout = view.findViewById(R.id.LayoutBackgroundCardView);
        CardView cardView = view.findViewById(R.id.cardView);
        checkCardView(cardView, linearLayout);
    }

    void checkCardView(CardView cardView, LinearLayout linearLayout) {
        if (cardView.getTag() == getString(R.string.cardView_unmark)) {
            cardView.setTag(getString(R.string.cardView_mark));
            linearLayout.setBackground(view.getContext().getDrawable(R.drawable.cardview_selected));
        } else {
            cardView.setTag(getString(R.string.cardView_unmark));
            linearLayout.setBackgroundColor(view.getContext().getColor(R.color.colorBlank));
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
        adapterBooksList.filter(s);
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
