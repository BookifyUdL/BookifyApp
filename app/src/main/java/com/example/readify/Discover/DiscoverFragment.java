package com.example.readify.Discover;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
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
import com.example.readify.ApiConnector;
import com.example.readify.MainActivity;
import com.example.readify.MockupsValues;
import com.example.readify.Models.Book;
import com.example.readify.Models.Genre;
import com.example.readify.Models.ServerCallback;
import com.example.readify.Models.ServerCallbackForBooks;
import com.example.readify.Models.User;
import com.example.readify.Pages;
import com.example.readify.R;
import com.example.readify.Design.ShadowTransformer;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
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
        /*CompoundButton.OnCheckedChangeListener*/ {

    private Button mButton;
    private ViewPager mViewPager;
    //List<Book> list = MockupsValues.getLastAddedBooks();
    int width, height;

    private CardPagerAdapter mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;
    private CardFragmentPagerAdapter mFragmentCardAdapter;
    private ShadowTransformer mFragmentCardShadowTransformer;
    private boolean mShowingFragments = false;

    private SharedPreferences prefs;
    private User user;

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
        final View view = inflater.inflate(R.layout.fragment_discover, container, false);

        prefs = getActivity().getSharedPreferences("com.example.readify", Context.MODE_PRIVATE);
        user = MockupsValues.getUser();

        //user = new User();
        //user.readFromSharedPreferences(prefs);

        mViewPager = (ViewPager) view.findViewById(R.id.viewPager);

        ImageView searchButton = (ImageView) view.findViewById(R.id.searchIcon);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSearchFragment();
            }
        });

        /* Latest additions list */
        mCardAdapter = new CardPagerAdapter((MainActivity) getActivity());
        for (int i = 0; i < MockupsValues.getLastAddedBooks().size(); i++) {
            mCardAdapter.addCardItem(MockupsValues.getLastAddedBooks().get(i));
        }
        mFragmentCardAdapter = new CardFragmentPagerAdapter(getFragmentManager(),
                dpToPixels(2, getContext()));

        mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardAdapter);
        mFragmentCardShadowTransformer = new ShadowTransformer(mViewPager, mFragmentCardAdapter);

        mViewPager.setAdapter(mCardAdapter);
        mViewPager.setPageTransformer(false, mCardShadowTransformer);
        mViewPager.setOffscreenPageLimit(3);

        /* Top rated list*/
        final LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.top_rated_recycler_view);
        final DiscoverFragment discoverFragment = this;
        ApiConnector.getTopRatedBooks(getContext(), new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                recyclerView.setLayoutManager(horizontalLayoutManager);
                BooksHorizontalAdapter adapter = new BooksHorizontalAdapter((MainActivity) getActivity(), getContext(), MockupsValues.getTopRatedBooks(), true, MockupsValues.user);
                adapter.setClickListener(discoverFragment);
                recyclerView.setAdapter(adapter);

                ApiConnector.getTopRatedBooksForGenre(getContext(), MockupsValues.user.getGenres(), new ServerCallbackForBooks() {
                    @Override
                    public void onSuccess(ArrayList<ArrayList<Book>> booksByGenre) {
                        System.out.println("Get top rated books by genre succeed");
                        addGenres(view, booksByGenre);
                    }

                    @Override
                    public void onSuccess(Book book){

                    }
                });
            }
        });

        width = view.getWidth();
        height = view.getHeight();

        return view;
    }

    private void addGenres(View view, ArrayList<ArrayList<Book>> booksByGenre) {
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.relative_layout);
        boolean isFirst = true;
        int previousId = 0;
        int previousCardId = 0;

        //ArrayList<Book> books = MockupsValues.getLastAddedBooksDiscover();
        //books.add(MockupsValues.getSameAuthorBooks().get(0));
        for (int i = 0; i < user.getGenres().size(); i++){
            TextView textView = new TextView(getContext());
            String text = user.getGenres().get(i).getName() + " genre";
            textView.setText(text);
            textView.setTextAppearance(R.style.TextAppearance_AppCompat_Body1);
            textView.setTypeface(null, Typeface.BOLD);
            textView.setTextColor(ContextCompat.getColor(getContext(), R.color.primaryText));
            textView.setTextSize(25);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins((int) getContext().getResources().getDimension(R.dimen.margin_15dp), (int) getContext().getResources().getDimension(R.dimen.margin_15dp), 0, 0);
            if (isFirst) {
                params.addRule(RelativeLayout.BELOW, R.id.card_view_top_rated);
                isFirst = false;
            } else {
                params.addRule(RelativeLayout.BELOW, previousCardId);
            }
            textView.setLayoutParams(params);
            previousId = ViewCompat.generateViewId();
            textView.setId(previousId);
            relativeLayout.addView(textView);


            float height = getContext().getResources().getDimension(R.dimen.card_height);
            CardView cardView = new CardView(getContext());
            RelativeLayout.LayoutParams cardParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) height);
            cardParams.setMargins((int) getContext().getResources().getDimension(R.dimen.margin_15dp), (int) getContext().getResources().getDimension(R.dimen.margin_15dp), (int) getContext().getResources().getDimension(R.dimen.margin_15dp), (int) getContext().getResources().getDimension(R.dimen.margin_15dp));
            cardParams.addRule(RelativeLayout.BELOW, previousId);
            cardParams.addRule(RelativeLayout.ALIGN_TOP);
            cardView.setLayoutParams(cardParams);
            cardView.setRadius((int) getContext().getResources().getDimension(R.dimen.card_corner_radius_big));
            previousCardId = ViewCompat.generateViewId();
            cardView.setId(previousCardId);
            cardView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryLight));


            RecyclerView recyclerView = new RecyclerView(getContext());
            RelativeLayout.LayoutParams recyclerParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            recyclerParams.setMargins((int) getContext().getResources().getDimension(R.dimen.margin_10dp), (int) getContext().getResources().getDimension(R.dimen.margin_10dp), (int) getContext().getResources().getDimension(R.dimen.margin_10dp), (int) getContext().getResources().getDimension(R.dimen.margin_10dp));
            recyclerView.setLayoutParams(recyclerParams);

            //List list = MockupsValues.getLastAddedBooks();
            LinearLayoutManager horizontalLayoutManager
                    = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(horizontalLayoutManager);
            BooksHorizontalAdapter adapter = new BooksHorizontalAdapter((MainActivity) getActivity(), getContext(), booksByGenre.get(i), true, user);
            adapter.setClickListener(this);
            recyclerView.setAdapter(adapter);

            cardView.addView(recyclerView);
            relativeLayout.addView(cardView);
        }

        int blankLayoutHeight = (int) getContext().getResources().getDimension(R.dimen.blank_layout);
        LinearLayout linearLayout = new LinearLayout(getContext());
        RelativeLayout.LayoutParams linearLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, blankLayoutHeight);
        linearLayoutParams.addRule(RelativeLayout.BELOW, previousCardId);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(linearLayoutParams);
        relativeLayout.addView(linearLayout);

    }

    @Override
    public void onItemClick(View view, int position, Book book, int size) {
        // Aqui pt esta el bug del discover
        if (position == size - 1) {
            showSearchFragment();
        } else {
            showBookFragment(book);
        }
    }

    private void showBookFragment(Book book) {
        MainActivity activity = (MainActivity) getActivity();
        activity.goToBookPage(book, Pages.DISCOVER_PAGE);
    }

    private void showSearchFragment() {
        MainActivity activity = (MainActivity) getActivity();
        activity.changeSearchBookFragment();
        //activity.changeDiscoverFragment();
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
}
