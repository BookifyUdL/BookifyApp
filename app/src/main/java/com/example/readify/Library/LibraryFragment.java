package com.example.readify.Library;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.readify.Adapters.BooksGridAdapter;
import com.example.readify.Adapters.BooksHorizontalAdapter;
import com.example.readify.FirstTimeForm.GenresFragment;
import com.example.readify.FirstTimeForm.RecyclerViewAdapterGenres;
import com.example.readify.MainActivity;
import com.example.readify.MockupsValues;
import com.example.readify.Models.Book;
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

    BooksGridAdapter booksAdapter;
    GridLayoutManager gridLayoutManager;

    private OnFragmentInteractionListener mListener;

    public LibraryFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static LibraryFragment newInstance() {
        LibraryFragment fragment = new LibraryFragment();
        return fragment;
    }

    public void notifyLibraryChanged(){
        booksAdapter.notifyDataSetChanged();
        //gridLayoutManager.notify();
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
        RecyclerView recyclerViewGenres = view.findViewById(R.id.recyclerViewGenres);
        gridLayoutManager = new GridLayoutManager(view.getContext(), 3);
        recyclerViewGenres.setLayoutManager(gridLayoutManager);

        //RecyclerViewAdapterGenres adapterGenres = new RecyclerViewAdapterGenres(getContext(), genres, MockupsValues.user);
        //adapterGenres.setClickListener(GenresFragment.this);
        booksAdapter = new BooksGridAdapter((MainActivity) getActivity(),getContext(), MockupsValues.user.getLibrary());
        recyclerViewGenres.setAdapter(booksAdapter);
        booksAdapter.setClickListener(this);

        SearchView searchView = view.findViewById(R.id.search_bar);
        searchView.setFocusable(false);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(this);
        return view;
    }

    @Override
    public void onItemClick(View view, int position) {
        showBookFragment(MockupsValues.user.getLibrary().get(position));
    }

    private void showBookFragment(Book book){
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
