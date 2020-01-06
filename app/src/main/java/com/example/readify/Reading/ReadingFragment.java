package com.example.readify.Reading;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.readify.Adapters.BooksListVerticalAdapter;
import com.example.readify.Adapters.SwipeToReadOrDeleteCallback;
import com.example.readify.MainActivity;
import com.example.readify.MockupsValues;
import com.example.readify.Models.Book;
import com.example.readify.Models.User;
import com.example.readify.R;
import com.wdullaer.swipeactionadapter.SwipeActionAdapter;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReadingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReadingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReadingFragment extends Fragment {

    private BooksListVerticalAdapter pendingBooksAdapter;
    private BooksListVerticalAdapter readingBooksAdapter;

    private RecyclerView recyclerView;
    private RecyclerView recyclerView2;

    private LinearLayout emptyLayout;
    private LinearLayout anyBookMessage;
    private LinearLayout readingTextLayout;
    private LinearLayout pendingTextLayout;

    private SharedPreferences prefs;

    private OnFragmentInteractionListener mListener;

    public ReadingFragment() {
        // Required empty public constructor
    }

    public static ReadingFragment newInstance() {
        ReadingFragment fragment = new ReadingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reading, container, false);

        prefs = view.getContext().getSharedPreferences("com.example.readify", Context.MODE_PRIVATE);

        LinearLayout discoverButton = view.findViewById(R.id.discover_layout);
        anyBookMessage = (LinearLayout) view.findViewById(R.id.any_book_layout);
        emptyLayout = (LinearLayout) view.findViewById(R.id.empty_layout);
        readingTextLayout = (LinearLayout) view.findViewById(R.id.reading_text_layout);
        pendingTextLayout = (LinearLayout) view.findViewById(R.id.pending_text_layout);
        discoverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.focusDiscoverFragment();
            }
        });

        /*ArrayList<Book> list = MockupsValues.user.getInterested();
        ArrayList<Book> list2 = MockupsValues.user.getReading();

        ArrayList<Book> pendingBooks = new ArrayList<>();
        ArrayList<Book> readingBooks = new ArrayList<>();

        for(Book b: MockupsValues.getAllBooksForTutorial()){
            if(list.contains(b))
                pendingBooks.add(b);
            if(list2.contains(b))
                readingBooks.add(b);

        }*/

        /* Reading books */
        LinearLayoutManager verticalLayoutManagaer = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.reading_books_recycler_view);
        recyclerView.setLayoutManager(verticalLayoutManagaer);

        readingBooksAdapter = new BooksListVerticalAdapter((MainActivity) getActivity(), getContext(),
                MockupsValues.user.getReading(), getActivity().getSupportFragmentManager(), MockupsValues.user);
        recyclerView.setAdapter(readingBooksAdapter);

        ItemTouchHelper itemTouchHelperReading = new ItemTouchHelper(new SwipeToReadOrDeleteCallback(readingBooksAdapter, false, getContext()));
        itemTouchHelperReading.attachToRecyclerView(recyclerView);

        /* Pending books */
        LinearLayoutManager vlm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView2 = (RecyclerView) view.findViewById(R.id.pending_books_recycler_view);
        recyclerView2.setLayoutManager(vlm);

        pendingBooksAdapter = new BooksListVerticalAdapter((MainActivity) getActivity(), getContext(),
                MockupsValues.user.getInterested(), MockupsValues.user);
        pendingBooksAdapter.setIsInPendingList(true);
        recyclerView2.setAdapter(pendingBooksAdapter);

        ItemTouchHelper itemTouchHelperDelete = new ItemTouchHelper(new SwipeToReadOrDeleteCallback(pendingBooksAdapter, true, getContext()));
        itemTouchHelperDelete.attachToRecyclerView(recyclerView2);

        shouldShowEmptyMessage();

        return view;
    }

    private void shouldShowEmptyMessage() {
        if (MockupsValues.user.getReading() == null) {
            MockupsValues.user.setReading(new ArrayList<Book>());
        }
        if (MockupsValues.user.getInterested() == null) {
            MockupsValues.user.setInterested(new ArrayList<Book>());
        }

        if (MockupsValues.user.getReading().isEmpty() && MockupsValues.user.getInterested().isEmpty()) {
            anyBookMessage.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
            pendingTextLayout.setVisibility(View.GONE);
            readingTextLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            recyclerView2.setVisibility(View.GONE);
        } else {
            anyBookMessage.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);
            pendingTextLayout.setVisibility(View.VISIBLE);
            readingTextLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView2.setVisibility(View.VISIBLE);
        }
    }

    public void readingBooksChanged() {
        readingBooksAdapter.setBooksList(MockupsValues.user.getReadingBooks());
        readingBooksAdapter.notifyDataSetChanged();
        shouldShowEmptyMessage();
    }

    public void pendingListChanged() {
        pendingBooksAdapter.setBooksList(MockupsValues.user.getInterested());
        pendingBooksAdapter.notifyDataSetChanged();
        shouldShowEmptyMessage();
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
