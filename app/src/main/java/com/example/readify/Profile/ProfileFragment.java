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
import android.widget.TextView;

import com.example.readify.Adapters.BooksHorizontalAdapter;
import com.example.readify.MainActivity;
import com.example.readify.MockupsValues;
import com.example.readify.Models.Book;
import com.example.readify.Models.User;
import com.example.readify.R;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements BooksHorizontalAdapter.ItemClickListener {
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
        user = MockupsValues.getUserProfile();

        // Put the name of the user
        TextView textViewNameUser = (TextView) view.findViewById(R.id.nameUserTextview);
        textViewNameUser.setText(user.getName());

        // Change the layout accord to the type of account
        ImageView imageViewPremiumBadge = (ImageView) view.findViewById(R.id.premiumBadgeProfile);
        TextView textViewTypeAccount = (TextView) view.findViewById(R.id.typeAccountText);
        TextView textViewPeriodAccount = (TextView) view.findViewById(R.id.periodPremium);

        if (user.isPremium()){
            imageViewPremiumBadge.setVisibility(View.VISIBLE);
            textViewTypeAccount.setTextColor(Color.rgb(219,192,0));
            textViewTypeAccount.setText(getString(R.string.accountPremiumText));
            textViewPeriodAccount.setVisibility(View.VISIBLE);
        } else
            textViewTypeAccount.setText(getString(R.string.accountFreeText));

        // Create a favourite books list on profile
        LinearLayoutManager favouriteBooksManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerViewFavouriteBooks = (RecyclerView) view.findViewById(R.id.recycler_view_profile_favourite_books);
        recyclerViewFavouriteBooks.setLayoutManager(favouriteBooksManager);

        // set up the RecyclerView
        List<Book> list = MockupsValues.getLastAddedBooks();
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewFavouriteBooks.setLayoutManager(horizontalLayoutManager);
        BooksHorizontalAdapter adapter = new BooksHorizontalAdapter((MainActivity) getActivity(), getContext(), list, true);
        adapter.setClickListener(this);
        recyclerViewFavouriteBooks.setAdapter(adapter);

        return view;
    }

    @Override
    public void onItemClick(View view, int position) {
        if(position == user.getLibrary().size()-1){
            showSearchFragment();
        } else {
            showBookFragment(user.getLibrary().get(position));
        }
    }

    private void showBookFragment(Book book){
        MainActivity activity = (MainActivity) getActivity();
        activity.goToBookPage(book);
    }

    private void showSearchFragment(){
        MainActivity activity = (MainActivity) getActivity();
        activity.changeDiscoverFragment();
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
