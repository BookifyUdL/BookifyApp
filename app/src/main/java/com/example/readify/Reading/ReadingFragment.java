package com.example.readify.Reading;

import android.content.Context;
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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    BooksListVerticalAdapter pendingBooksAdapter;
    BooksListVerticalAdapter readingBooksAdapter;
    RecyclerView recyclerView;
    RecyclerView recyclerView2;
    SwipeActionAdapter readingBooksSwipeAdapter;
    private LinearLayout anyBookMessage;
    private LinearLayout readingTextLayout;
    private LinearLayout pendingTextLayout;

    private OnFragmentInteractionListener mListener;

    public ReadingFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ReadingFragment newInstance() {
        ReadingFragment fragment = new ReadingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reading, container, false);
        LinearLayout discoverButton = view.findViewById(R.id.discover_layout);
        anyBookMessage = (LinearLayout) view.findViewById(R.id.any_book_layout);
        readingTextLayout = (LinearLayout) view.findViewById(R.id.reading_text_layout);
        pendingTextLayout = (LinearLayout) view.findViewById(R.id.pending_text_layout);
        discoverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.focusDiscoverFragment();
            }
        });

        /*Llista baix*/
        LinearLayoutManager verticalLayoutManagaer = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.reading_books_recycler_view);
        recyclerView.setLayoutManager(verticalLayoutManagaer);
        ArrayList<Book> list = new ArrayList<>();
        //list.add(MockupsValues.getLastAddedBooks().get(0));
        readingBooksAdapter = new BooksListVerticalAdapter((MainActivity) getActivity(), getContext(), list, true, getActivity().getSupportFragmentManager());
        recyclerView.setAdapter(readingBooksAdapter);
        ItemTouchHelper itemTouchHelperReading = new ItemTouchHelper(new SwipeToReadOrDeleteCallback(readingBooksAdapter, false));
        itemTouchHelperReading.attachToRecyclerView(recyclerView);


        LinearLayoutManager vlm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView2 = (RecyclerView) view.findViewById(R.id.pending_books_recycler_view);
        recyclerView2.setLayoutManager(vlm);
        ArrayList<Book> pendingBooksList = new ArrayList<>();
        pendingBooksList.addAll(MockupsValues.getPendingListBooks());
        pendingBooksAdapter = new BooksListVerticalAdapter((MainActivity) getActivity(), getContext(), pendingBooksList);
        pendingBooksAdapter.setIsInPendingList(true);
        recyclerView2.setAdapter(pendingBooksAdapter);
        ItemTouchHelper itemTouchHelperDelete = new ItemTouchHelper(new SwipeToReadOrDeleteCallback(pendingBooksAdapter, true));
        itemTouchHelperDelete.attachToRecyclerView(recyclerView2);
        shouldShowEmptyMessage();
        return view;
    }

    private void shouldShowEmptyMessage(){
        if(MockupsValues.getReandingListBooks().isEmpty() && MockupsValues.getPendingListBooks().isEmpty()){
            anyBookMessage.setVisibility(View.VISIBLE);
            pendingTextLayout.setVisibility(View.INVISIBLE);
            readingTextLayout.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
            recyclerView2.setVisibility(View.INVISIBLE);
        } else {
            anyBookMessage.setVisibility(View.INVISIBLE);
            pendingTextLayout.setVisibility(View.VISIBLE);
            readingTextLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView2.setVisibility(View.VISIBLE);
        }

    }

    public void readingBooksChanged(){
        ArrayList<Book> readingBooks = MockupsValues.getReandingListBooks();
        readingBooksAdapter.setBooksList(readingBooks);
        readingBooksAdapter.notifyDataSetChanged();
        shouldShowEmptyMessage();
    }

    public void pendingListChanged(){
        ArrayList<Book> pendingBooks = MockupsValues.getPendingListBooks();
        pendingBooksAdapter.setBooksList(pendingBooks);
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
