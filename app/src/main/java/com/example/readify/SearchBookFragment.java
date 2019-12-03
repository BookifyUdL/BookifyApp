package com.example.readify;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;

import com.example.readify.Adapters.BooksListVerticalAdapter;
import com.example.readify.Models.Book;
import com.example.readify.Models.User;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchBookFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchBookFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchBookFragment extends Fragment implements SearchView.OnQueryTextListener {

    private OnFragmentInteractionListener mListener;
    private BooksListVerticalAdapter adapter;
    private ImageView goBackButton;

    private SharedPreferences prefs;
    private User user;

    public SearchBookFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchBookFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchBookFragment newInstance(String param1, String param2) {
        SearchBookFragment fragment = new SearchBookFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_book, container, false);

        prefs = view.getContext().getSharedPreferences("com.example.readify", Context.MODE_PRIVATE);
        user = new User();
        user.readFromSharedPreferences(prefs);

        goBackButton = (ImageView) view.findViewById(R.id.go_back_button);
        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onGoBackButtonClicked();
            }
        });

        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.books_recycler_view);
        recyclerView.setLayoutManager(verticalLayoutManager);

        ArrayList<Book> list  = new ArrayList<>();
        //TODO Change for obtain all books from database?
        list.addAll(MockupsValues.getLastAddedBooks());
        list.addAll(MockupsValues.getSameAuthorBooks());
        list.addAll(MockupsValues.getSameGenderBooks());

        adapter = new BooksListVerticalAdapter((MainActivity) getActivity(), getContext(), list, user);
        recyclerView.setAdapter(adapter);

        SearchView searchView = view.findViewById(R.id.search_bar);
        searchView.setFocusable(false);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(this);

        return view;
    }

    private void onGoBackButtonClicked(){
        MainActivity activity = (MainActivity) getActivity();
        activity.backToDiscoverFragment();
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.filter(newText);
        return false;
    }

}
