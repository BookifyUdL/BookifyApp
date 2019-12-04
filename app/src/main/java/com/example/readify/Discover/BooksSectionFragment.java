package com.example.readify.Discover;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.readify.Adapters.BooksListVerticalAdapter;
import com.example.readify.MainActivity;
import com.example.readify.MockupsValues;
import com.example.readify.Models.Book;
import com.example.readify.Models.User;
import com.example.readify.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class BooksSectionFragment extends Fragment {

    private BooksListVerticalAdapter adapter;

    private User user;

    public BooksSectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_books_section, container, false);

        user = new User();
        user.readFromSharedPreferences(getActivity().getSharedPreferences("com.example.readify", Context.MODE_PRIVATE));

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

        return view;
    }

    public void displayReceivedData(String word)
    {
        adapter.filter(word);
    }
}
