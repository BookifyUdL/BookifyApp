package com.example.readify.Library;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.example.readify.Adapters.BooksGridAdapter;
import com.example.readify.Adapters.BooksHorizontalAdapter;
import com.example.readify.FirstTimeForm.GenresFragment;
import com.example.readify.FirstTimeForm.RecyclerViewAdapterGenres;
import com.example.readify.MainActivity;
import com.example.readify.MockupsValues;
import com.example.readify.Models.Book;
import com.example.readify.Models.User;
import com.example.readify.Pages;
import com.example.readify.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LibraryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LibraryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LibraryFragment extends Fragment implements SearchView.OnQueryTextListener, BooksHorizontalAdapter.ItemClickListener {

    private BooksGridAdapter booksAdapter;
    private GridLayoutManager gridLayoutManager;
    private RecyclerView recyclerViewGenres;
    private LinearLayout anyBookLayout;
    private LinearLayout searchLayout;
    private SearchView searchView;

    private User user;
    private SharedPreferences prefs;

    private OnFragmentInteractionListener mListener;

    public LibraryFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static LibraryFragment newInstance() {
        LibraryFragment fragment = new LibraryFragment();
        return fragment;
    }

    private void shouldShowEmptyMessage() {
        if (booksAdapter.getItemCount() == 0) {
            anyBookLayout.setVisibility(View.VISIBLE);
            searchLayout.setVisibility(View.GONE);
            recyclerViewGenres.setVisibility(View.GONE);
        } else {
            anyBookLayout.setVisibility(View.GONE);
            searchLayout.setVisibility(View.VISIBLE);
            recyclerViewGenres.setVisibility(View.VISIBLE);
        }
    }

    public void notifyLibraryChanged() {
        user.readFromSharedPreferences(prefs);
        booksAdapter.setBooksList(user.getLibrary());
        booksAdapter.notifyDataSetChanged();
        shouldShowEmptyMessage();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_library, container, false);

        user = new User();
        prefs = getActivity().getSharedPreferences("com.example.readify", Context.MODE_PRIVATE);
        user.readFromSharedPreferences(prefs);

        LinearLayout discoverButton = view.findViewById(R.id.discover_layout);
        discoverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.focusDiscoverFragment();
            }
        });
        searchLayout = (LinearLayout) view.findViewById(R.id.search_bar_bookshelf);
        anyBookLayout = (LinearLayout) view.findViewById(R.id.any_book_layout);
        recyclerViewGenres = view.findViewById(R.id.recyclerViewGenres);
        gridLayoutManager = new GridLayoutManager(view.getContext(), 3);
        recyclerViewGenres.setLayoutManager(gridLayoutManager);

        //RecyclerViewAdapterGenres adapterGenres = new RecyclerViewAdapterGenres(getContext(), genres, MockupsValues.user);
        //adapterGenres.setClickListener(GenresFragment.this);
        booksAdapter = new BooksGridAdapter((MainActivity) getActivity(), getContext(), user.getLibrary(), user);
        recyclerViewGenres.setAdapter(booksAdapter);
        booksAdapter.setClickListener(this);

        searchView = view.findViewById(R.id.search_bar);
        searchView.setFocusable(false);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(this);
        shouldShowEmptyMessage();
        return view;
    }

    @Override
    public void onItemClick(View view, int position, Book book, int size) {
        showBookFragment(book);
    }

    private void showBookFragment(Book book) {
        MainActivity activity = (MainActivity) getActivity();
        activity.goToBookPage(book, Pages.LIBRARY_PAGE);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        booksAdapter.filter(newText);
        return false;
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
