package com.example.readify.Profile;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.readify.Adapters.AchievementsHoritzontalAdapter;
import com.example.readify.Adapters.BooksHorizontalAdapter;
import com.example.readify.Adapters.BooksProfileHoritzontalAdapter;
import com.example.readify.Adapters.GenresHoritzontalAdapter;
import com.example.readify.MainActivity;
import com.example.readify.MockupsValues;
import com.example.readify.Models.Achievement;
import com.example.readify.Models.Book;
import com.example.readify.Models.Genre;
import com.example.readify.Models.User;
import com.example.readify.Pages;
import com.example.readify.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements BooksProfileHoritzontalAdapter.ItemClickListener, GenresHoritzontalAdapter.ItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private User user;

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        user = MockupsValues.getUser();

        // Put the name of the user
        TextView textViewNameUser = (TextView) view.findViewById(R.id.nameUserTextview);
        textViewNameUser.setText(user.getName());

        // Change the layout accord to the type of account
        ImageView imageViewPremiumBadge = (ImageView) view.findViewById(R.id.premiumBadgeProfile);
        if (user.isPremium())
            imageViewPremiumBadge.setVisibility(View.VISIBLE);
        else
            imageViewPremiumBadge.setVisibility(View.INVISIBLE);

        // Num of readed books by the user
        TextView readedBooksTextView = (TextView) view.findViewById(R.id.numReadedBooksText);
        readedBooksTextView.setText(Integer.toString(user.getReadedBooks().size()));

        // Achievements of the user
        TextView textViewAchievements = (TextView) view.findViewById(R.id.achievementsText);
        textViewAchievements.setText(user.getNumCompletedAchievements() + getResources().getString(R.string.diagonalBar) + user.getAchievements().size());

        // Create a genre preferences list on profile
        LinearLayoutManager genrePreferencesManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerViewGenrePreferences = (RecyclerView) view.findViewById(R.id.recycler_view_profile_genre_preferences);
        recyclerViewGenrePreferences.setLayoutManager(genrePreferencesManager);

        ArrayList<Genre> listGenresPreferences = user.getGenres();
        GenresHoritzontalAdapter adapterGenrePreferences = new GenresHoritzontalAdapter(getContext(), listGenresPreferences);
        adapterGenrePreferences.setClickListener(this);
        recyclerViewGenrePreferences.setAdapter(adapterGenrePreferences);

        // Create a readed books list on profile
        LinearLayoutManager readedBooksManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerViewReadedBooks = (RecyclerView) view.findViewById(R.id.recycler_view_profile_favourite_books);
        recyclerViewReadedBooks.setLayoutManager(readedBooksManager);

        List<Book> listReadedBooks = user.getReadedBooks();
        BooksProfileHoritzontalAdapter adapterReaderBooks =
                new BooksProfileHoritzontalAdapter(getContext(), listReadedBooks);
        adapterReaderBooks.setClickListener(this);
        recyclerViewReadedBooks.setAdapter(adapterReaderBooks);

        // Create a achievements list on profile
        LinearLayoutManager achievementsManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerViewAchievements = (RecyclerView) view.findViewById(R.id.recycler_view_profile_achievements);
        recyclerViewAchievements.setLayoutManager(achievementsManager);

        List<Achievement> achievementsCompleted = user.getCompletedAchievements();
        AchievementsHoritzontalAdapter adapterAchievements = new AchievementsHoritzontalAdapter(getContext(), achievementsCompleted);
        recyclerViewAchievements.setAdapter(adapterAchievements);

        return view;
    }

    @Override
    public void onItemClick(View view, int position) {
        MainActivity activity = (MainActivity) getActivity();
        if (user.getReadedBooks().size() == position)
            activity.focusDiscoverFragment();
        else
            activity.goToBookPage(user.getReadedBooks().get(position), Pages.PROFILE_PAGE);
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

    @Override
    public void onItemClick(View view) {
        MainActivity activity = (MainActivity) getActivity();
        activity.goToFirstForm();
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
