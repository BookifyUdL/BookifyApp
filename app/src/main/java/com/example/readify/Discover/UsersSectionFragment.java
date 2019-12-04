package com.example.readify.Discover;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.readify.Adapters.BooksListVerticalAdapter;
import com.example.readify.Adapters.UserListVerticalAdapter;
import com.example.readify.MainActivity;
import com.example.readify.MockupsValues;
import com.example.readify.Models.Book;
import com.example.readify.Models.User;
import com.example.readify.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class UsersSectionFragment extends Fragment {

    private UserListVerticalAdapter adapter;

    private User user;

    public UsersSectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users_section, container, false);

        user = new User();
        user.readFromSharedPreferences(getActivity().getSharedPreferences("com.example.readify", Context.MODE_PRIVATE));

        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.users_recycler_view);
        recyclerView.setLayoutManager(verticalLayoutManager);

        ArrayList<User> list  = new ArrayList<>();
        //TODO Change for obtain all users from database?
        list.addAll(MockupsValues.getUsers());

        adapter = new UserListVerticalAdapter((MainActivity) getActivity(), getContext(), list, user);
        recyclerView.setAdapter(adapter);

        return view;
    }

    public void displayReceivedData(String word)
    {
        adapter.filter(word);
    }
}
