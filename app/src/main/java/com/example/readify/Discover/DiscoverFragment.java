package com.example.readify.Discover;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.readify.Adapters.BooksHorizontalAdapter;
import com.example.readify.Adapters.CardFragmentPagerAdapter;
import com.example.readify.Adapters.CardPagerAdapter;
import com.example.readify.MainActivity;
import com.example.readify.MockupsValues;
import com.example.readify.Models.Book;
import com.example.readify.R;
import com.example.readify.Design.ShadowTransformer;

import android.widget.Button;
import android.widget.ImageView;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DiscoverFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DiscoverFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiscoverFragment extends Fragment implements View.OnClickListener,
        BooksHorizontalAdapter.ItemClickListener
        /*CompoundButton.OnCheckedChangeListener*/{

    private Button mButton;
    private ViewPager mViewPager;
    List<Book> list;
    int width, height;

    private CardPagerAdapter mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;
    private CardFragmentPagerAdapter mFragmentCardAdapter;
    private ShadowTransformer mFragmentCardShadowTransformer;
    private boolean mShowingFragments = false;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public DiscoverFragment() {
        // Required empty public constructor
    }


    public static DiscoverFragment newInstance(/*String param1, String param2*/) {
        DiscoverFragment fragment = new DiscoverFragment();
        /*Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
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
        View view = inflater.inflate(R.layout.fragment_discover, container, false);
        mViewPager = (ViewPager) view.findViewById(R.id.viewPager);

        final ImageView searchButton = (ImageView) view.findViewById(R.id.searchIcon);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSearchFragment();
            }
        });


        mCardAdapter = new CardPagerAdapter((MainActivity) getActivity());
        for (int i=0; i < MockupsValues.getLastAddedBooks().size(); i++){
            mCardAdapter.addCardItem(MockupsValues.getLastAddedBooks().get(i));
        }
        mFragmentCardAdapter = new CardFragmentPagerAdapter(getFragmentManager(),
                dpToPixels(2, getContext()));

        mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardAdapter);
        mFragmentCardShadowTransformer = new ShadowTransformer(mViewPager, mFragmentCardAdapter);

        mViewPager.setAdapter(mCardAdapter);
        mViewPager.setPageTransformer(false, mCardShadowTransformer);
        mViewPager.setOffscreenPageLimit(3);

        /*Llista baix*/
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.top_rated_recycler_view);
        recyclerView.setLayoutManager(horizontalLayoutManagaer);
        // set up the RecyclerView
        list = MockupsValues.getLastAddedBooks();
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        BooksHorizontalAdapter adapter = new BooksHorizontalAdapter((MainActivity) getActivity(), getContext(), list, true);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        width = view.getWidth();
        height = view.getHeight();
        return view;
    }

    @Override
    public void onItemClick(View view, int position) {
        if(position == list.size()-1){
            showSearchFragment();
        } else {
            showBookFragment(list.get(position));
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

    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }

    @Override
    public void onClick(View view) {
        if (!mShowingFragments) {
            mButton.setText("Views");
            mViewPager.setAdapter(mFragmentCardAdapter);
            mViewPager.setPageTransformer(false, mFragmentCardShadowTransformer);
        } else {
            mButton.setText("Fragments");
            mViewPager.setAdapter(mCardAdapter);
            mViewPager.setPageTransformer(false, mCardShadowTransformer);
        }

        mShowingFragments = !mShowingFragments;
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

   /* @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        mCardShadowTransformer.enableScaling(b);
        mFragmentCardShadowTransformer.enableScaling(b);
    }*/
}
