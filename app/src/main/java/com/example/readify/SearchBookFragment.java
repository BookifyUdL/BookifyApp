package com.example.readify;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;

import com.example.readify.Adapters.BooksListVerticalAdapter;
import com.example.readify.Discover.BooksSectionFragment;
import com.example.readify.Discover.UsersSectionFragment;
import com.example.readify.Discover.ViewPageAdapter;
import com.example.readify.Models.User;
import com.google.android.material.tabs.TabLayout;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchBookFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchBookFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchBookFragment extends Fragment implements SearchView.OnQueryTextListener {

    private static final int BOOK_FRAGMENT_ACTIVE = 0;
    private static final int USER_FRAGMENT_ACTIVE = 1;

    private OnFragmentInteractionListener mListener;
    private ImageView goBackButton;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPageAdapter viewPageAdapter;
    private ComunicateFragments comunicateFragments;
    private int active;
    private String actualText;

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

        active = 0;
        actualText = "";

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

        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        viewPageAdapter = new ViewPageAdapter(getActivity().getSupportFragmentManager(), 0);

        viewPageAdapter.addFragment(new BooksSectionFragment(), "Books");
        viewPageAdapter.addFragment(new UsersSectionFragment(), "Users");
        viewPager.setAdapter(viewPageAdapter);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        active = BOOK_FRAGMENT_ACTIVE;
                        onQueryTextChange(actualText);
                        break;
                    case 1:
                        active = USER_FRAGMENT_ACTIVE;
                        onQueryTextChange(actualText);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        SearchView searchView = view.findViewById(R.id.search_bar);
        searchView.setFocusable(false);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(this);

        return view;
    }

    private void onGoBackButtonClicked() {
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

        try {
            comunicateFragments = (ComunicateFragments) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Error in retrieving data. Please try again");
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
        comunicateFragments = null;
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
        if (active == BOOK_FRAGMENT_ACTIVE)
            comunicateFragments.sendWord(newText, viewPageAdapter.getItem(0), active);
        else
            comunicateFragments.sendWord(newText, viewPageAdapter.getItem(1), active);

        actualText = newText;
        return false;
    }

    // Interface to send a data
    public interface ComunicateFragments {
        void sendWord(String word, Fragment fragment, int active);
    }

}
